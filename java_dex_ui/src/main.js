import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store/'
import ElementUI from 'element-ui'
import  'echarts'
import ECharts from 'vue-echarts'
import 'element-ui/lib/theme-default/index.css'
import axios from 'axios'
import qs from 'qs';
Vue.use(axios)
Vue.config.productionTip = false
Vue.component('ECharts', ECharts)
Vue.prototype.$axios = axios
Vue.use(ElementUI);
Vue.prototype.$qs = qs;
new Vue({
	el: '#app',
	router,
	store,
	template: '<App/>',
	components: { App }
})
