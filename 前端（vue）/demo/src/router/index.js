import { createRouter, createWebHashHistory } from 'vue-router'


const routes = [
  {
    path: '/',
    redirect:'/manager',
    
  },
  {
    path: '/login',
    name: 'login',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/login.vue')
  },
  {
    path: '/manager',
    name: 'manager',
    component: () => import(/* webpackChunkName: "about" */ '../views/manager.vue'),
  },
  {
    path: '/words',
    name: 'words',
    component: () => import(/* webpackChunkName: "about" */ '../views/words.vue'),
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
