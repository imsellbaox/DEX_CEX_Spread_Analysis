<template>


    <div class="fillcontain">
        <head-top></head-top>
        <div class="table_container">
            <!--        <div style="height: 300px;width:400px"  ref="chart"></div>-->
            <el-table
                :data="tableData"
                @expand='expand'
                :expand-row-keys='expendRow'
                :row-key="row => row.index"
                style="width: 100%">
                <el-table-column
                    label="分时"
                    width="240px">
                    <el-select :v-model="valuet" placeholder="请选择" clearable @change="$forceUpdate()">
                        <el-option
                            v-for="(item,index) in selects"
                            :key="index"
                            :label="item.label"
                            :value="item.value"
                            @click.native="mybd(item)">
                        </el-option>
                    </el-select>
                </el-table-column>
                <el-table-column
                    label="平台对"
                    prop="abdex"
                    width="240px">
                </el-table-column>
                <el-table-column
                    label="价差"
                >
                    <e-charts class="chart" :option="options"/>
                </el-table-column>
                <el-table-column
                    label="币对"
                    prop="symbol"
                    width="160px"
                >
                </el-table-column>

            </el-table>
        </div>
    </div>
</template>
<script>
import headTop from '../components/headTop'
import {baseUrl, baseImgPath} from '@/config/env'
import {formatDate} from "element-ui/packages/date-picker/src/util";
export default {
    data(){
        return {
            baseUrl,
            baseImgPath,
            tableData: [],
            expendRow: [],
            tableData2: [],
            selects: [{
                value: 1,
                label: '秒数据'
            }, {
                value: 1*60,
                label: '分钟数据'
            }, {
                value: 1*60*15,
                label: '15分钟数据'
            }, {
                value: 1*60*60,
                label: '小时数据'
            }
            ],
            valuet: '',
        }
    },
    created(){
        if (this.initVData()){
            this.getEcharts();
        }

    },
    components: {
        headTop,
    },
    computed: {
        options() {
            return this.option();
        }
    },
    methods: {
        option() {
            var option ={
                //dataZoom-inside 内置型数据区域缩放组件 所谓内置 1平移：在坐标系中滑动拖拽进行数据区域平移。2缩放：PC端：鼠标在坐标系范围内滚轮滚动（MAC触控板类同;移动端：在移动端触屏上，支持两指滑动缩放。
                dataZoom: [{
                    type: 'inside', //1平移 缩放
                    throttle: '50', //设置触发视图刷新的频率。单位为毫秒（ms）。
                    minValueSpan: 6, //用于限制窗口大小的最小值,在类目轴上可以设置为 5 表示 5 个类目
                    start: 0, //数据窗口范围的起始百分比 范围是：0 ~ 100。表示 0% ~ 100%。
                    end: 50, //数据窗口范围的结束百分比。范围是：0 ~ 100。
                    zoomLock: true, //如果设置为 true 则锁定选择区域的大小，也就是说，只能平移，不能缩放。
                }],
                // 主要用来控制图表四周留白
                grid: {
                    left: '25px',
                    top: '10%',
                },
                // 提示框组件
                tooltip: {

                    trigger: 'axis', //坐标轴触发，主要在柱状图，折线图等会使用类目轴的图表中使用。
                    backgroundColor: '#e0ec81', //提示框浮层的背景颜色。
                    axisPointer: { //去掉移动的指示线
                        type: 'none'

                    },
                    // 自定义提示框内容
                    formatter: function (params, ticket, callback) {
                        var text = params[0].data.ts + " || 价差（A-B）:" + params[0].data.value+'<br>'
                                    + params[0].data.adexdd
                                    + params[0].data.bdexdd;
                        return text;
                    }
                },
                //直角坐标系 grid 中的 x 轴，
                xAxis: {
                    type: 'category', //'category' 类目轴，适用于离散的类目数据，为该类型时必须通过 data 设置类目数据。
                    // 坐标轴轴线相关设置
                    axisLine: {
                        lineStyle: {
                            color: '#E5E5E5', // 坐标轴线线的颜色。
                        }
                    },
                    // 坐标轴刻度标签(类目,简单说就是x轴上的内容)的相关设置
                    axisLabel: {
                        //  是否显示坐标刻度标签(这了指是否显示x轴上的月份)
                        show: true,
                        // 标签文字的颜色
                        color: '#999'
                    },
                    //x轴刻度线设置
                    axisTick: {
                        "show": true
                    },
                    // 类目数据，在类目轴（type: 'category'）中有效。
                    data:  this.tableData2.map(subData => subData.indextime),
                },
                //直角坐标系 grid 中的 y 轴，
                yAxis: {
                    type: 'value', //'value' 数值轴，适用于连续数据。
                    // 坐标轴轴线相关设置
                    axisLine: {
                        show: true //y轴线消失
                    },
                    // 坐标轴刻度标签(类目,简单说就是x轴上的内容)的相关设置
                    axisLabel: {
                        show: true,
                        // 标签文字的颜色
                        color: '#999'
                    },
                    //y轴刻度线设置
                    axisTick: {
                        "show": true
                    },
                    splitNumber: 5, //坐标轴的分割段数，需要注意的是这个分割段数只是个预估值，最后实际显示的段数会在这个基础上根据分割后坐标轴刻度显示的易读程度作调整.在类目轴中无效。
                },
                series: [{
                    type: 'line', //折线图是用折线将各个数据点标志连接起来的图表，用于展现数据的变化趋势。和全局设置type效果一样,表示折线图
                    // 系列中的数据内容数组。数组项通常为具体的数据项。
                    data: this.tableData2,
                    // 折线条的样式
                    lineStyle: {
                        color: '#377CFF',
                        width: 1
                    },
                    // 折线拐点的样式
                    itemStyle: {
                        normal: { // 静止时：
                            color: '#377CFF',
                        },
                        emphasis: { // 鼠标经过时：
                            color: '#377CFF',
                        }
                    },
                    symbol: 'circle', //拐点样式
                    symbolSize: 4, //拐点大小
                }]
            };
            return option
        },

        mybd(item){
            this.tableData2 = [];
            const e = this.tableData[0]
            console.log(e);
            if (item.value == 1){
                this.$axios({
                    // 默认请求方式为get
                    method: 'post',
                    url: 'http://localhost:8080/dex/data/pricesub/second',
                    // 传递参数
                    data: e,
                }).then(res => {

                    res.data.forEach((item, index) => {
                        const subData = {};
                        let date = new Date(item.ts);
                        subData.ts = formatDate(date, 'MM月dd日—hh:mm:ss');
                        subData.indextime = formatDate(date,'hh:mm:ss')
                        subData.value = item.pricesub.toFixed(3);
                        subData.adexdd = item.adexdd;
                        subData.bdexdd = item.bdexdd;
                        this.tableData2.push(subData);
                    })
                })
            };
            if (item.value == 1*60){
                this.$axios({
                    // 默认请求方式为get
                    method: 'post',
                    url: 'http://localhost:8080/dex/data/pricesub/min',
                    // 传递参数
                    data: e,
                }).then(res => {
                    res.data.forEach((item, index) => {
                        const subData = {};
                        let date = new Date(item.ts);
                        subData.ts = formatDate(date, 'MM月dd日—hh:mm');
                        subData.indextime = formatDate(date,'hh:mm')
                        subData.value = item.pricesub.toFixed(3);
                        subData.adexdd = item.adexdd;
                        subData.bdexdd = item.bdexdd;
                        this.tableData2.push(subData);
                    })
                })
            };
            if (item.value == 1*60*15){
                this.$axios({
                    // 默认请求方式为get
                    method: 'post',
                    url: 'http://localhost:8080/dex/data/pricesub/eqmin',
                    // 传递参数
                    data: e,
                }).then(res => {
                    res.data.forEach((item, index) => {
                        const subData = {};
                        let date = new Date(item.ts);
                        subData.ts = formatDate(date, 'MM月dd日—hh:mm');
                        subData.indextime = formatDate(date,'hh:mm')
                        subData.value = item.pricesub.toFixed(3);
                        subData.adexdd = item.adexdd;
                        subData.bdexdd = item.bdexdd;
                        this.tableData2.push(subData);
                    })
                })
            };
            if (item.value == 1*60*60){
                this.$axios({
                    // 默认请求方式为get
                    method: 'post',
                    url: 'http://localhost:8080/dex/data/pricesub/hour',
                    // 传递参数
                    data: e,
                }).then(res => {
                    res.data.forEach((item, index) => {
                        const subData = {};
                        let date = new Date(item.ts);
                        subData.ts = formatDate(date, 'MM月dd日—hh');
                        subData.indextime = formatDate(date,'MM-dd-—hh')
                        subData.value = item.pricesub.toFixed(3);
                        subData.adexdd = item.adexdd;
                        subData.bdexdd = item.bdexdd;
                        this.tableData2.push(subData);
                    })
                })
            };
            setTimeout(()=>{},2000)
            this.$forceUpdate()

        },

        async initVData(){
            this.$axios.get("/dex/task/all").then(res => {
                res.data.forEach((item, index) => {
                    var Data = {};
                    Data.a_dex = item.a_dex;
                    Data.b_dex = item.b_dex;
                    Data.symbol = item.symbol;
                    Data.depth_level = item.depth_level;
                    Data.status = item.status;
                    Data.ts = item.ts;
                    Data.abdex= item.a_dex+"/"+item.b_dex;
                    Data.index = index;
                    this.tableData.push(Data);
                })
            })
            return true
        },
        async optiones (item) {
            this.getSubData(item);
            setTimeout(() =>{
                if (item == null) {
                    return
                }

                return {
                    xAxis: {
                        type: 'category',
                        data: this.tableData2.map(subData => subData.ts)
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [
                        {
                            data: this.tableData2.map(subData => subData.pricesub),
                            type: 'line',
                            smooth: true
                        }
                    ]
                }
            },1000)

        },
        async getSubData(item){
            this.$axios({
                // 默认请求方式为get
                method: 'post',
                url: 'http://localhost:8080/dex/data/pricesub/second',
                // 传递参数
                data: item,
            }).then(res => {
                this.tableData2 = [];
                res.data.forEach((item, index) => {
                    const subData = {};
                    let date = new Date(item.ts);
                    subData.ts = formatDate(date, 'M月dd日—hh:mm:ss');
                    subData.indextime = formatDate(date,'hh:mm:ss')
                    subData.value = item.pricesub.toFixed(3);
                    subData.adexdd = item.adexdd;
                    subData.bdexdd = item.bdexdd;
                    this.tableData2.push(subData);
                })
            })
            return true;
        },

        async getEcharts() {
            this.$axios.get("/dex/task/all").then(res => {
                res.data.forEach((item, index) => {
                    this.optiones(item)
                })
            })
        },
        expand(row, status){
            if (status) {
                this.getSelectItemData(row)
            }else{
                const index = this.expendRow.indexOf(row.index);
                this.expendRow.splice(index, 1)
            }
        },
        async getSelectItemData(row, type){
            this.expendRow.push(row.index);
        },
    },
    filters: {
        // 时间格式自定义 只需把字符串里面的改成自己所需的格式
        formatDate(time) {
            let date = new Date(time);
            return formatDate(date, 'MM.dd.hh.mm.ss');
        },
        formatDate2(time) {
            let date = new Date(time);
            return formatDate(date, 'hh:mm:ss');
        },
        formatDate3(time) {
            let date = new Date(time);
            return formatDate(date, 'yyyy年MM月dd日 hh:mm:ss');
        },
        numFilter (value) {
            // 截取当前数据到小数点后两位
            let realVal = parseFloat(value).toFixed(2)
            return realVal
        }

    },
}
</script>

<style scoped>
.chart {
    height: 800px;
    width: 1100px;
}
</style>
