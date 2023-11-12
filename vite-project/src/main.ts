import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import { createPinia } from 'pinia'
import { createPersistedState } from 'pinia-plugin-persistedstate';


const pinia = createPinia()
pinia.use(createPersistedState())
const app = createApp(App)
app.use(pinia)
app.mount('#app')