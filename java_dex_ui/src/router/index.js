import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)


const manage = r => require.ensure([], () => r(require('@/page/manage')), 'manage');
const home = r => require.ensure([], () => r(require('@/page/home')), 'home');
const addShop = r => require.ensure([], () => r(require('@/page/addShop')), 'addShop');
const taskList = r => require.ensure([], () => r(require('@/page/taskList')), 'taskList');
const visitor = r => require.ensure([], () => r(require('@/page/visitor')), 'visitor');

const routes = [
	{
		path: '/',
		component: manage
	},
	{
		path: '/manage',
		component: manage,
		name: '',
		children: [{
			path: '',
			component: home,
			meta: [],
		},{
			path: '/addShop',
			component: addShop,
			meta: ['添加数据', '创建任务'],
		},{
			path: '/tasklist',
			component: taskList,
			meta: ['数据管理', '任务表'],
		},{
			path: '/visitor',
			component: visitor,
			meta: ['图表', '价差分析'],
		}]
	}
]

export default new Router({
	routes,
	strict: process.env.NODE_ENV !== 'production',
})
