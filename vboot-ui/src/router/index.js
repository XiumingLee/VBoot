import Vue from 'vue'
import Router from 'vue-router'


Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    name: 'Home',
    meta: { title: '首页', icon: 'fa-home' },
    children: [{
      path: 'home',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'fa-home' }
    },
    {
      path:'info',
      component:()=>import('@/views/dashboard/info'),
      meta:{ title:'个人资料' },
      hidden: true 
    }
  ]
  },
  {
    path:"/chat",
    component: Layout,
    name:'Chat',
    hidden: true,
    children:[{
      path: 'chatPage',
      component: () => import('@/views/additional/chat'),
      meta: { title: '聊天互动', icon: 'fa-weixin' },
      hidden: true
    }]
  }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})
