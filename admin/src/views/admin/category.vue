<template>
  <div>
    <div class="row">
      <div class="col-md-6">
        <p>
          <button v-show="hasResource(BUSINESS_CATEGORY_MANAGEMENT_UPDATE)" v-on:click="addParent()" class="btn btn-white btn-default btn-round">
            <i class="ace-icon fa fa-edit"></i>
            新增一级
          </button>
          &nbsp;
          <button v-on:click="all()" class="btn btn-white btn-default btn-round">
            <i class="ace-icon fa fa-refresh"></i>
            刷新
          </button>
        </p>
        <table v-show="hasResource(BUSINESS_CATEGORY_MANAGEMENT_BROWSER)" id="level1-table" class="table  table-bordered table-hover">
          <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>顺序</th>
            <th v-show="hasResource(BUSINESS_CATEGORY_MANAGEMENT_UPDATE)">操作</th>
          </tr>
          </thead>

          <tbody>
          <tr v-for="parent in level1" v-on:click="toChild(parent)" v-bind:class="{'active' : active.id === parent.id}">
            <td>{{parent.id}}</td>
            <td>{{parent.name}}</td>
            <td>{{parent.sort}}</td>
            <td v-show="hasResource(BUSINESS_CATEGORY_MANAGEMENT_UPDATE)">
              <div class="ace-settings-item btn-group clearfix">
                <button v-on:click="edit(parent)" class="btn btn-xs btn-info">
                  <i class="ace-icon fa fa-pencil bigger-120"></i>
                </button>&nbsp;
                <button v-show="hasResource(BUSINESS_CATEGORY_MANAGEMENT_DELETE)" v-on:click="del(parent.id)" class="btn btn-xs btn-danger">
                  <i class="ace-icon fa fa-trash-o bigger-120"></i>
                </button>&nbsp;
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <div class="col-md-6" v-show="hasResource(BUSINESS_CATEGORY_MANAGEMENT_BROWSER)">
        <p>
          <button v-show="hasResource(BUSINESS_CATEGORY_MANAGEMENT_UPDATE)" v-on:click="addChild()" class="btn btn-white btn-default btn-round">
            <i class="ace-icon fa fa-edit"></i>
            新增二级
          </button>
        </p>
        <table id="level2-table" class="table  table-bordered table-hover">
          <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>顺序</th>
            <th v-show="hasResource(BUSINESS_CATEGORY_MANAGEMENT_UPDATE)">操作</th>
          </tr>
          </thead>

          <tbody>
          <tr v-for="child in level2">
            <td>{{child.id}}</td>
            <td>{{child.name}}</td>
            <td>{{child.sort}}</td>
            <td v-show="hasResource(BUSINESS_CATEGORY_MANAGEMENT_UPDATE)">
              <div class="ace-settings-item btn-group clearfix">
                <button v-on:click="edit(child)" class="btn btn-xs btn-info">
                  <i class="ace-icon fa fa-pencil bigger-120"></i>
                </button>
                <button v-show="hasResource(BUSINESS_CATEGORY_MANAGEMENT_DELETE)" v-on:click="del(child.id)" class="btn btn-xs btn-danger">
                  <i class="ace-icon fa fa-trash-o bigger-120"></i>
                </button>
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">表单</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label class="col-sm-2 control-label">父级分类</label>
                <div class="col-sm-10">
                  <p class="form-control-static">{{active.name || "无"}}</p>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">名称</label>
                <div class="col-sm-10">
                  <input v-model="category.name" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">顺序</label>
                <div class="col-sm-10">
                  <input v-model="category.sort" class="form-control">
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button v-show="hasResource(BUSINESS_CATEGORY_MANAGEMENT_UPDATE)" v-on:click="save()" type="button" class="btn btn-primary">保存</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
  </div>
</template>

<script>

  export default {
    name: "business-category",
    data: function() {
      return {
        category: {},
        categoryArray: [],
        arr: ['00000000'],
        level1: [],
        level2: [],
        active: {},
        BUSINESS_CATEGORY_MANAGEMENT_BROWSER : BUSINESS_CATEGORY_MANAGEMENT_BROWSER,
        BUSINESS_CATEGORY_MANAGEMENT_UPDATE : BUSINESS_CATEGORY_MANAGEMENT_UPDATE,
        BUSINESS_CATEGORY_MANAGEMENT_DELETE : BUSINESS_CATEGORY_MANAGEMENT_DELETE
      }
    },
    mounted: function() {
      let _this = this;
      _this.all();
    },

    methods: {

      /**
       * 查找是否有权限
       * @param id
       */
      hasResource(id) {
        return Tool.hasResource(id);
      },

      login () {
        this.$router.push("/admin")
      },

      /**
       * 点击【新增】父级分类
       */
      addParent() {
        let _this = this;
        _this.active = [];
        _this.category = {
          parent : _this.arr[0]
        };
        $("#form-modal").modal("show");
      },

      /**
       * 点击【新增】子级分类
       */
      addChild() {
        let _this = this;
        if (Tool.isEmpty(_this.active)) {
          Toast.warning("请先点击父级分类!");
          return;
        }
        _this.category = {
          parent: _this.active.id
        };
        $("#form-modal").modal("show");
      },

      /**
       * 点击【编辑】
       */
      edit(category) {
        let _this = this;
        if (category.parent === _this.arr[0]) {
          _this.active = {};
        }
        _this.category = $.extend({}, category);
        $("#form-modal").modal("show");
      },


      /**
       * 列表查询
       */
      all() {
        let _this = this;
        Loading.show();
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/admin/category/all', {
        }).then((response)=>{
          Loading.hide();
          _this.categoryArray = response.data.data;

          _this.level1 = [];
          for (let i = 0; i < _this.categoryArray.length; i++) {

            if (_this.categoryArray[i].parent === _this.arr[0] || _this.categoryArray[i].parent === _this.arr[1]) {
              let parent = _this.categoryArray[i];
              _this.level1.push(parent);

              for (let j = 0; j < _this.categoryArray.length; j++) {

                let child = _this.categoryArray[j];
                if (Tool.isEmpty(parent.children)) {
                  parent.children = [];
                }
                if (child.parent === parent.id) {
                  parent.children.push(child);
                }

              }
            }
          }
          // 解决添加子类分级不刷新问题
          _this.level2 = [];
          setTimeout(function () {
            $("tr.active").trigger("click");
          },10);
        })
      },

      /**
       * 点击【跳转到子级分类】
       */
      toChild(category) {
        let _this = this;
        _this.active = category;
        _this.level2 = category.children;
      },

      /**
       * 点击【保存】
       */
      save() {
        let _this = this;

        // 保存校验
        if (1 !== 1 || !Validator.length(_this.category.name, "名称", 1, 50)
        ) {
          return;
        }

        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/category/save', _this.category).then((response)=>{
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            $("#form-modal").modal("hide");
            _this.all();
            Toast.success("保存成功！");
          } else {
            Toast.warning(resp.message)
          }
        })
      },

      /**
       * 点击【删除】
       */
      del(id) {
        let _this = this;
        Confirm.show("删除分类后不可恢复，确认删除？", function () {
          Loading.show();
          _this.$ajax.delete(process.env.VUE_APP_SERVER + '/business/admin/category/delete/' + id).then((response)=>{
            Loading.hide();
            if (response.data.success) {
              _this.all();
              Toast.success("删除成功！");
            }
          })
        });
      }
    }
  }
</script>
<style scoped>
.active td {
  background-color: #d6e9c6 !important;
}
</style>