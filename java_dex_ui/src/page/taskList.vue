<template>
    <div class="fillcontain">
        <head-top></head-top>
        <div class="table_container">
            <el-table
                :data="tableData" style="width: 100%">
                <el-table-column
                  label="平台A"
                  prop="Aname">
                </el-table-column>
                <el-table-column
                    label="平台B"
                    prop="Bname">
                </el-table-column>
                <el-table-column
                    label="币种对"
                    prop="symbol">
                </el-table-column>
                <el-table-column
                  label="深度"
                  prop="depth">
                </el-table-column>
                <el-table-column label="操作" width="240">
                  <template slot-scope="scope">
                      <el-button
                          size="small"
                          type="info"
                          v-show="!scope.row.status"
                          @click="handleStart(scope.$index, scope.row)">启动</el-button>
                      <el-button
                          size="small"
                          type="waring"
                          v-show="scope.row.status"
                          @click="handleClose(scope.$index, scope.row)">停止</el-button>
                    <el-button
                      size="small"
                      type="danger"
                      @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                  </template>
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>
    import headTop from '../components/headTop'
    import {baseUrl, baseImgPath} from '@/config/env'
    export default {
        data(){
            return {
                baseUrl,
                baseImgPath,
                restaurant_id: null,
                city: {},
                offset: 0,
                limit: 20,
                count: 0,
                currentPage: 1,
                tableData:[],
                selectTable: {},
                dialogFormVisible: false,
                menuOptions: [],
                selectMenu: {},
                selectIndex: null,
                specsForm: {
		          	specs: '',
		          	packing_fee: 0,
		          	price: 20,
		        },
                specsFormrules: {
		        	specs: [
						{ required: true, message: '请输入规格', trigger: 'blur' },
					],
		        },
		        specsFormVisible: false,
                expendRow: [],
            }
        },
        created(){
            this.initData();
        },
        computed: {
        },
    	components: {
    		headTop,
    	},
        methods: {
            async initData(){
                this.$axios.get("/dex/task/all").then(res => {
                    this.tableData = [];
                    res.data.forEach((item, index) => {
                        const tableData = {};
                        tableData.Aname = item.a_dex;
                        tableData.Bname = item.b_dex;
                        tableData.symbol = item.symbol;
                        tableData.depth = item.depth_level;
                        tableData.status = item.status;
                        tableData.ts = item.ts;
                        tableData.index = index;
                        this.tableData.push(tableData);
                    })
                })
            },
		    addspecs(){
				this.specs.push({...this.specsForm});
				this.specsForm.specs = '';
				this.specsForm.packing_fee = 0;
				this.specsForm.price = 20;
				this.specsFormVisible = false;
			},
            handleSizeChange(val) {
                console.log(`每页 ${val} 条`);
            },
            handleCurrentChange(val) {
                this.currentPage = val;
                this.offset = (val - 1)*this.limit;
                this.getFoods()
            },
            async handleStart(index, row) {
                this.$axios.post("/dex/task/start/"+row.ts).then(res => {
                        this.initData();
                });

            },
            async handleClose(index, row) {
                this.$axios.post("/dex/task/close/"+row.ts).then(res => {
                        this.initData();
                });

            },
            async handleDelete(index, row) {
                this.$axios.post("/dex/task/delete/"+row.ts).then(res => {
                console.log(row.ts);
                this.initData();
                this.$forceUpdate();
                });
            },
        },
    }
</script>

<style lang="less">
	@import '../style/mixin';
    .demo-table-expand {
        font-size: 0;
    }
    .demo-table-expand label {
        width: 90px;
        color: #99a9bf;
    }
    .demo-table-expand .el-form-item {
        margin-right: 0;
        margin-bottom: 0;
        width: 50%;
    }
    .table_container{
        padding: 20px;
    }
    .Pagination{
        display: flex;
        justify-content: flex-start;
        margin-top: 8px;
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
</style>
