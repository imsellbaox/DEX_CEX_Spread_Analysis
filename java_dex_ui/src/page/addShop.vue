<template>
    <div>
        <head-top></head-top>
        <el-row style="margin-top: 20px;">
            <el-col :span="12" :offset="4">
                <el-form :model="formData"  ref="formData" label-width="110px" class="demo-formData">
                    <el-form-item label="平台A">
                        <el-cascader
                            :options="defaultOptions"
                            v-model="value1"
                            change-on-select
                        ></el-cascader>
                    </el-form-item>

                    <el-form-item label="平台B">
                        <el-cascader
                            :options="defaultOptions"
                            v-model="value2"
                            change-on-select
                        ></el-cascader>
                    </el-form-item>
                    <el-form-item label="币对类型" >
                        <el-input v-model="formData.symbol"></el-input>
                    </el-form-item>
                    <el-form-item label="深度" >
                        <el-input v-model="formData.depth_level"></el-input>
                    </el-form-item>
                    <el-form-item class="button_submit">
                        <el-button type="primary" @click="submitForm('formData')">立即创建</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import headTop from '@/components/headTop'
export default {
    data(){
        return {
            city: {},
            formData: {
                ts: 0,
                a_dex: '',
                b_dex: '',
                symbol:'',
                depth_level: 5,
                status: false,
                exsit: true

            },

            value1: [],
            value2: [],
            defaultOptions: [
                {
                value: 'Binance',
                label: '币安',
            },
                {
                    value: 'PancakeSwap',
                    label: 'PancakeSwap',
                }
            ],
        }
    },
    components: {
        headTop,
    },
    mounted(){
    },
    methods: {
        submitForm(formName) {
            var flag = false;
            this.$axios.get("/dex/task/all").then(res => {

                if (res.data.length > 0){
                    flag = true

                }

            })
            setTimeout(() =>{

            if (flag){
                this.$notify.error({
                    title: '错误',
                    message: '仅能创建一个任务，作者前端太差，未优化',
                    offset: 100
                });

                return
            }
            if (this.value1.length == 0 || this.value2.length == 0 ){
                this.$notify.error({
                    title: '错误',
                    message: '未选择平台',
                    offset: 100
                });
                return
            }
            var a = String(this.value1);
            var b = String(this.value2);

            if (a == b ){
                this.$notify.error({
                    title: '错误',
                    message: '平台不能相同',
                    offset: 100
                });
                return
            }
            if (this.formData.symbol == ""){
                this.$notify.error({
                    title: '错误',
                    message: '币对不能为空',
                    offset: 100
                });
                return
            }
            this.$refs[formName].validate(async (valid) => {
                if (valid) {
                    this.formData.ts= new Date().getTime();
                    this.formData.a_dex=a;
                    this.formData.b_dex=b;
                    this.$axios({
                        // 默认请求方式为get
                        method: 'post',
                        url: 'http://localhost:8080/dex/task/insert',
                        // 传递参数
                        data: this.formData,
                    }).then(res => {
                        if (res.data){
                            this.$notify.success({
                                title: '成功！',
                                message: '成功插入task',
                                offset: 100
                            });
                            this.$forceUpdate();
                        }
                    })

                } else {
                    this.$notify.error({
                        title: '错误',
                        message: '请检查输入是否正确',
                        offset: 100
                    });
                    return false;
                }
            });
            },1000)
        },
    }
}
</script>

<style lang="less">
@import '../style/mixin';
.button_submit{
    text-align: center;
}
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}
.avatar-uploader .el-upload:hover {
    border-color: #20a0ff;
}
.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 120px;
    height: 120px;
    line-height: 120px;
    text-align: center;
}
.avatar {
    width: 120px;
    height: 120px;
    display: block;
}
.el-table .info-row {
    background: #c9e5f5;
}

.el-table .positive-row {
    background: #e2f0e4;
}
</style>
