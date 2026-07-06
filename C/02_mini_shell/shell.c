#define _POSIX_C_SOURCE 200809L /* necesario para strtok_r con -std=c11 */

#include <fcntl.h>
#include <limits.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>

#define MAX_LINE 1024
#define MAX_ARGS 64
#define MAX_STAGES 16

#ifndef PATH_MAX
#define PATH_MAX_LOCAL 4096
#else
#define PATH_MAX_LOCAL PATH_MAX
#endif

typedef struct {
  char *argv[MAX_ARGS];
  int argc;
  char *infile;
  char *outfile;
  int append; /* 1 si la redireccion de salida es >> */
} Command;

static void print_prompt(void) {
  char cwd[PATH_MAX_LOCAL];
  if (getcwd(cwd, sizeof(cwd)) != NULL)
    printf("mini-shell:%s$ ", cwd);
  else
    printf("mini-shell$ ");
  fflush(stdout);
}

/* PATH_MAX no siempre esta en <limits.h> segun el sistema; definimos un tope
 * propio */
#ifndef PATH_MAX_LOCAL
#define PATH_MAX_LOCAL 4096
#endif

static void sigint_handler(int signo) {
  (void)signo;
  write(STDOUT_FILENO, "\n", 1);
}

/* Divide 'line' por el caracter '|' en hasta MAX_STAGES trozos (tokenizados
 * aparte) */
static int split_pipeline(char *line, char *stages[]) {
  int n = 0;
  char *saveptr;
  char *tok = strtok_r(line, "|", &saveptr);
  while (tok != NULL && n < MAX_STAGES) {
    stages[n++] = tok;
    tok = strtok_r(NULL, "|", &saveptr);
  }
  return n;
}

/* Tokeniza un stage por espacios y extrae redirecciones (<, >, >>) y args
 * normales */
static void parse_stage(char *stage, Command *cmd) {
  memset(cmd, 0, sizeof(*cmd));
  char *saveptr;
  char *tok = strtok_r(stage, " \t\n", &saveptr);

  while (tok != NULL) {
    if (strcmp(tok, "<") == 0) {
      tok = strtok_r(NULL, " \t\n", &saveptr);
      if (tok)
        cmd->infile = tok;
    } else if (strcmp(tok, ">>") == 0) {
      tok = strtok_r(NULL, " \t\n", &saveptr);
      if (tok) {
        cmd->outfile = tok;
        cmd->append = 1;
      }
    } else if (strcmp(tok, ">") == 0) {
      tok = strtok_r(NULL, " \t\n", &saveptr);
      if (tok) {
        cmd->outfile = tok;
        cmd->append = 0;
      }
    } else {
      if (cmd->argc < MAX_ARGS - 1)
        cmd->argv[cmd->argc++] = tok;
    }
    tok = strtok_r(NULL, " \t\n", &saveptr);
  }
  cmd->argv[cmd->argc] = NULL;
}

/* Built-ins que deben ejecutarse en el proceso del propio shell */
static int run_builtin(Command *cmd, int *exit_flag) {
  if (cmd->argc == 0)
    return 1;

  if (strcmp(cmd->argv[0], "exit") == 0) {
    *exit_flag = 1;
    return 1;
  }
  if (strcmp(cmd->argv[0], "cd") == 0) {
    const char *target = cmd->argc > 1 ? cmd->argv[1] : getenv("HOME");
    if (target == NULL || chdir(target) != 0)
      fprintf(stderr, "cd: no se pudo cambiar a '%s'\n",
              target ? target : "(vacio)");
    return 1;
  }
  if (strcmp(cmd->argv[0], "pwd") == 0) {
    char cwd[PATH_MAX_LOCAL];
    if (getcwd(cwd, sizeof(cwd)) != NULL)
      printf("%s\n", cwd);
    return 1;
  }
  if (strcmp(cmd->argv[0], "help") == 0) {
    printf("mini-shell: cd, pwd, exit, help; soporta pipes (|), redirecciones "
           "(< > >>) y background (&)\n");
    return 1;
  }
  return 0; /* no era un built-in */
}

/* Aplica las redirecciones de un stage sobre los descriptores estandar del
 * proceso actual */
static void apply_redirections(Command *cmd) {
  if (cmd->infile) {
    int fd = open(cmd->infile, O_RDONLY);
    if (fd < 0) {
      perror(cmd->infile);
      _exit(1);
    }
    dup2(fd, STDIN_FILENO);
    close(fd);
  }
  if (cmd->outfile) {
    int flags = O_WRONLY | O_CREAT | (cmd->append ? O_APPEND : O_TRUNC);
    int fd = open(cmd->outfile, flags, 0644);
    if (fd < 0) {
      perror(cmd->outfile);
      _exit(1);
    }
    dup2(fd, STDOUT_FILENO);
    close(fd);
  }
}

static void execute_pipeline(Command cmds[], int n, int background) {
  int prev_read = -1;
  pid_t pids[MAX_STAGES];

  for (int i = 0; i < n; i++) {
    int pipefd[2] = {-1, -1};
    int has_next = (i < n - 1);
    if (has_next && pipe(pipefd) < 0) {
      perror("pipe");
      return;
    }

    pid_t pid = fork();
    if (pid < 0) {
      perror("fork");
      return;
    }

    if (pid == 0) {
      /* proceso hijo: Ctrl+C debe funcionar normal aqui */
      signal(SIGINT, SIG_DFL);

      if (prev_read != -1) {
        dup2(prev_read, STDIN_FILENO);
        close(prev_read);
      }
      if (has_next) {
        close(pipefd[0]);
        dup2(pipefd[1], STDOUT_FILENO);
        close(pipefd[1]);
      }
      apply_redirections(&cmds[i]);

      execvp(cmds[i].argv[0], cmds[i].argv);
      fprintf(stderr, "%s: comando no encontrado\n", cmds[i].argv[0]);
      _exit(127);
    }

    pids[i] = pid;
    if (prev_read != -1)
      close(prev_read);
    if (has_next) {
      close(pipefd[1]);
      prev_read = pipefd[0];
    }
  }

  if (background) {
    printf("[bg pid %d]\n", pids[n - 1]);
    return;
  }
  for (int i = 0; i < n; i++) {
    int status;
    waitpid(pids[i], &status, 0);
  }
}

/* Recoge zombies de procesos en background sin bloquear */
static void reap_background(void) {
  int status;
  pid_t pid;
  while ((pid = waitpid(-1, &status, WNOHANG)) > 0) {
    printf("[bg pid %d terminado]\n", pid);
  }
}

int main(void) {
  char line[MAX_LINE];
  int exit_flag = 0;

  signal(SIGINT, sigint_handler);

  while (!exit_flag) {
    reap_background();
    print_prompt();

    if (fgets(line, sizeof(line), stdin) == NULL) {
      printf("\n");
      break;
    }
    if (line[0] == '\n')
      continue;

    int background = 0;
    char *nl = strchr(line, '\n');
    if (nl)
      *nl = '\0';

    char *trimmed_end = line + strlen(line) - 1;
    while (trimmed_end > line && *trimmed_end == ' ')
      trimmed_end--;
    if (*trimmed_end == '&') {
      background = 1;
      *trimmed_end = '\0';
    }

    char *stages[MAX_STAGES];
    int n_stages = split_pipeline(line, stages);
    if (n_stages == 0)
      continue;

    Command cmds[MAX_STAGES];
    for (int i = 0; i < n_stages; i++)
      parse_stage(stages[i], &cmds[i]);

    if (cmds[0].argc == 0)
      continue;

    if (n_stages == 1 && run_builtin(&cmds[0], &exit_flag)) {
      continue;
    }

    execute_pipeline(cmds, n_stages, background);
  }

  return 0;
}
