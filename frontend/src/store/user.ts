import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { LoginResponse, MenuItem } from '@/types'

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<Partial<LoginResponse>>(
    JSON.parse(localStorage.getItem('userInfo') || '{}')
  )
  const permissions = ref<string[]>([])

  const isLoggedIn = computed(() => !!token.value)
  const role = computed(() => userInfo.value.role)
  const username = computed(() => userInfo.value.username)
  const realName = computed(() => userInfo.value.realName)

  const menuList = computed<MenuItem[]>(() => {
    const userRole = role.value
    const menus: Record<string, MenuItem[]> = {
      ADMIN: [
        { path: '/dashboard', name: '仪表盘', icon: 'Odometer' },
        { path: '/user', name: '用户管理', icon: 'User' },
        { path: '/owner', name: '业主管理', icon: 'House' },
        { path: '/repair', name: '报修管理', icon: 'Tools' },
        { path: '/payment', name: '缴费管理', icon: 'Money' },
        { path: '/notice', name: '公告管理', icon: 'Bell' },
        { path: '/building', name: '楼栋管理', icon: 'OfficeBuilding' }
      ],
      REPAIRER: [
        { path: '/my-repair', name: '我的报修', icon: 'Tools' },
        { path: '/repair-record', name: '维修记录', icon: 'List' }
      ],
      OWNER: [
        { path: '/my-repair', name: '我的报修', icon: 'Tools' },
        { path: '/my-payment', name: '我的缴费', icon: 'Money' },
        { path: '/notice', name: '公告通知', icon: 'Bell' }
      ]
    }
    return menus[userRole || ''] || []
  })

  function setToken(newToken: string) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  function setUserInfo(info: LoginResponse) {
    userInfo.value = info
    permissions.value = info.permissions || []
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  function setPermissions(perms: string[]) {
    permissions.value = perms
  }

  function hasPermission(perm: string): boolean {
    return permissions.value.includes(perm)
  }

  function logout() {
    token.value = ''
    userInfo.value = {}
    permissions.value = []
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  return {
    token,
    userInfo,
    permissions,
    isLoggedIn,
    role,
    username,
    realName,
    menuList,
    setToken,
    setUserInfo,
    setPermissions,
    hasPermission,
    logout
  }
})
