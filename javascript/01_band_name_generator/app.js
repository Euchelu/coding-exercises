'use strict'

// ── Datos ──────────────────────────────────────────────────────────────────
const DATA = {
  adjectives: [
    'Cósmico','Fugitivo','Electrostático','Melancólico','Radioactivo',
    'Noctámbulo','Oxidado','Glorioso','Efímero','Mutante',
    'Abismal','Clandestino','Hipnótico','Errante','Galáctico',
    'Visceral','Anacrónico','Etéreo','Caótico','Sublime',
  ],
  nouns: [
    'Pulpo','Cenicero','Faro','Laberinto','Torpedo',
    'Ventilador','Oráculo','Búnker','Bonsái','Espejo',
    'Cactus','Sarcófago','Péndulo','Casete','Girasol',
    'Maniquí','Submarino','Acordeón','Esqueleto','Limbo',
  ],
  genres: [
    'Math Rock','Witch House','Cumbia Espacial','Doom Jazz',
    'Shoegazer','Post-Tropical','Ska Progresivo','Grunge Flamenco',
    'Synth Cumbia','Death Folk','Reggaetón Épico','Noise Bolero',
    'Black Metal Ibérico','Trap Barroco','Glam Punk Andaluz',
    'Vaporwave Canario','Emo Ranchero','Ambient Cañí','Industrial Sevillanas',
  ],
  icons: ['🎸','🥁','🎺','🎷','🎻','🪗','🎹','🎙️','🤘','🎤','🦇','🚀','💀','🌊','🔥'],
}

const pick = arr => arr[Math.floor(Math.random() * arr.length)]

function generate() {
  const adj   = pick(DATA.adjectives)
  const noun  = pick(DATA.nouns)
  const genre = pick(DATA.genres)
  const icon  = pick(DATA.icons)

  // patrones de nombre variados
  const patterns = [
    `${adj} ${noun}`,
    `Los ${noun}s ${adj}s`,
    `El ${noun} ${adj}`,
    `${noun} ${adj} Experience`,
    `${adj} ${noun} Collective`,
    `${noun} & The ${adj}s`,
  ]
  const name = pick(patterns)

  return { name, genre, icon }
}

// ── Estado ─────────────────────────────────────────────────────────────────
let current = null
let favorites = JSON.parse(localStorage.getItem('band-favorites') || '[]')

// ── DOM ────────────────────────────────────────────────────────────────────
const elName    = document.getElementById('result-name')
const elGenre   = document.getElementById('result-genre')
const elIcon    = document.getElementById('result-icon')
const elCard    = document.getElementById('result-card')
const btnGen    = document.getElementById('btn-generate')
const btnSave   = document.getElementById('btn-save')
const btnShare  = document.getElementById('btn-share')
const favList   = document.getElementById('favorites-list')
const favCount  = document.getElementById('fav-count')
const emptyMsg  = document.getElementById('empty-msg')

// ── Renderizado ─────────────────────────────────────────────────────────────
function renderResult({ name, genre, icon }) {
  // reset animation
  elCard.classList.remove('pop')
  void elCard.offsetWidth   // reflow
  elCard.classList.add('pop')

  elName.textContent  = name
  elGenre.textContent = genre
  elIcon.textContent  = icon
  elCard.style.borderColor = 'rgba(59,130,246,0.35)'
  elCard.style.boxShadow   = '0 0 40px rgba(59,130,246,0.1)'
  setTimeout(() => {
    elCard.style.borderColor = ''
    elCard.style.boxShadow   = ''
  }, 600)
}

function renderFavorites() {
  favList.innerHTML = ''
  favCount.textContent = favorites.length
  emptyMsg.style.display = favorites.length ? 'none' : 'block'
  btnShare.style.display = favorites.length ? 'inline-flex' : 'none'

  favorites.forEach((fav, i) => {
    const li = document.createElement('li')
    li.innerHTML = `
      <span style="font-size:1.3rem;flex-shrink:0">${fav.icon}</span>
      <div class="fav-info">
        <div class="fav-name">${fav.name}</div>
        <div class="fav-genre">${fav.genre}</div>
      </div>
      <button class="btn-remove" data-index="${i}" title="Eliminar">✕</button>
    `
    favList.appendChild(li)
  })

  // Check si el actual ya está guardado
  updateSaveBtn()
}

function updateSaveBtn() {
  const already = current && favorites.some(f => f.name === current.name)
  btnSave.disabled = !current || already
  btnSave.innerHTML = already
    ? '<span>✅</span> Guardado'
    : '<span>❤️</span> Guardar favorito'
}

function saveFavorites() {
  localStorage.setItem('band-favorites', JSON.stringify(favorites))
}

// ── Eventos ─────────────────────────────────────────────────────────────────
btnGen.addEventListener('click', () => {
  current = generate()
  renderResult(current)
  btnSave.disabled = false
  updateSaveBtn()
})

btnSave.addEventListener('click', () => {
  if (!current) return
  if (favorites.some(f => f.name === current.name)) return
  favorites.unshift({ ...current })
  saveFavorites()
  renderFavorites()
})

favList.addEventListener('click', e => {
  const btn = e.target.closest('.btn-remove')
  if (!btn) return
  const i = parseInt(btn.dataset.index, 10)
  favorites.splice(i, 1)
  saveFavorites()
  renderFavorites()
})

btnShare.addEventListener('click', () => {
  const text = favorites.map((f, i) => `${i + 1}. ${f.icon} ${f.name} (${f.genre})`).join('\n')
  navigator.clipboard.writeText(text).then(() => {
    btnShare.textContent = '✅ Copiado'
    setTimeout(() => { btnShare.innerHTML = '📋 Copiar lista' }, 2000)
  })
})

// ── Init ────────────────────────────────────────────────────────────────────
renderFavorites()
