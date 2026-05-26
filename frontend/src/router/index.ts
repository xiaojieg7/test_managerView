import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/common/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/views/common/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { requiresAuth: true, roles: ['ADMIN'] }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/admin/User.vue'),
        meta: { requiresAuth: true, roles: ['ADMIN'] }
      },
      {
        path: 'owner',
        name: 'Owner',
        component: () => import('@/views/admin/Owner.vue'),
        meta: { requiresAuth: true, roles: ['ADMIN'] }
      },
      {
        path: 'repair',
        name: 'Repair',
        component: () => import('@/views/admin/Repair.vue'),
        meta: { requiresAuth: true, roles: ['ADMIN'] }
      },
      {
        path: 'payment',
        name: 'Payment',
        component: () => import('@/views/admin/Payment.vue'),
        meta: { requiresAuth: true, roles: ['ADMIN'] }
      },
      {
        path: 'notice',
        name: 'Notice',
        component: () => import('@/views/admin/Notice.vue'),
        meta: { requiresAuth: true, roles: ['ADMIN', 'OWNER'] }
      },
      {
        path: 'building',
        name: 'Building',
        component: () => import('@/views/admin/Building.vue'),
        meta: { requiresAuth: true, roles: ['ADMIN'] }
      },
      {
        path: 'my-repair',
        name: 'MyRepair',
        component: () => import('@/views/owner/MyRepair.vue'),
        meta: { requiresAuth: true, roles: ['OWNER', 'REPAIRER'] }
      },
      {
        path: 'repair-record',
        name: 'RepairRecord',
        component: () => import('@/views/repairer/RepairRecord.vue'),
        meta: { requiresAuth: true, roles: ['REPAIRER'] }
      },
      {
        path: 'my-payment',
        name: 'MyPayment',
        component: () => import('@/views/owner/MyPayment.vue'),
        meta: { requiresAuth: true, roles: ['OWNER'] }
      }
    ]
  },
  {
    path: '/403',
    name: 'Forbidden',
    component: () => import('@/views/common/403.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/common/404.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, _from, next) => {
  const userStore = useUserStore()

  if (to.meta.requiresAuth !== false) {
    if (!userStore.isLoggedIn) {
      next('/login')
      return
    }

    const roles = to.meta.roles as string[] | undefined
    if (roles && !roles.includes(userStore.role || '')) {
      next('/403')
      return
    }
  }

  if (to.path === '/login' && userStore.isLoggedIn) {
    next('/')
    return
  }

  next()
})

export default router
