<template>
  <div>
    <p>
      <button v-show="hasResource(BUSINESS_COURSE_MANAGEMENT_UPDATE)" v-on:click="add()" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-edit"></i>
        新增
      </button>
      &nbsp;
      <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh"></i>
        刷新
      </button>
    </p>

    <pagination ref="pagination" v-bind:list="list" v-bind:itemCount="8"></pagination>
    <div class="row" v-show="hasResource(BUSINESS_COURSE_MANAGEMENT_BROWSER)">
      <div v-for="course in courses" class="col-md-4">
         <div class="thumbnail search-thumbnail">
            <img v-show="!course.image" class="media-object" src="/static/image/demo-course.jpg" />
            <img v-show="course.image" class="media-object" v-bind:src="course.image" />

           <div class="caption">
             <div class="clearfix">
              <span class="pull-right label label-primary info-label">
                {{COURSE_LEVEL | optionKV(course.level)}}
              </span>
               <span class="pull-right label label-primary info-label">
                {{COURSE_CHARGE | optionKV(course.charge)}}
              </span>
               <span class="pull-right label label-primary info-label">
                {{COURSE_STATUS | optionKV(course.status)}}
              </span>
             </div>

             <h3 class="search-title">
               <a href="#" class="blue">{{course.name}}</a>
             </h3>

             <div v-for="teacher in teachers.filter(t=>{return t.id===course.teacherId})" class="profile-activity clearfix">
               <div>
                 <img v-show="!teacher.image" class="pull-left" src="/ace/assets/images/avatars/avatar5.png">
                 <img v-show="teacher.image" class="pull-left" v-bind:src="teacher.image">
                 <a class="user" href="#"> {{teacher.name}} </a>
                 <br>
                 {{teacher.position}}
               </div>
             </div>

             <p>
               <span class="blue bolder bigger-150">{{course.price}}&nbsp;<i class="fa fa-rmb"></i></span>&nbsp;
             </p>
             <p>{{course.summary}}</p>
             <p>
               <span class="badge badge-info">{{course.id}}</span>
               <span class="badge badge-info">排序：{{course.sort}}</span>
               <span class="badge badge-info">{{course.time | formatSecond}}</span>
             </p>
             <p>
               <button v-on:click="toChapter(course)" class="btn btn-white btn-xs btn-info btn-round">
                 大章
               </button>&nbsp;
               <button v-on:click="toContent(course)" class="btn btn-white btn-xs btn-info btn-round">
                 内容
               </button>&nbsp;
               <button v-show="hasResource(BUSINESS_COURSE_MANAGEMENT_UPDATE)" v-on:click="openSortModal(course)" class="btn btn-white btn-xs btn-info btn-round">
                 排序
               </button>&nbsp;
               <button v-show="hasResource(BUSINESS_COURSE_MANAGEMENT_UPDATE)" v-on:click="edit(course)" class="btn btn-white btn-xs btn-info btn-round">
                 编辑
               </button>&nbsp;
               <button v-show="hasResource(BUSINESS_COURSE_MANAGEMENT_DELETE)" v-on:click="del(course.id)" class="btn btn-white btn-xs btn-warning btn-round">
                 删除
               </button>
             </p>
           </div>
        </div>
      </div>
    </div>

    <div id="course-form-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">表单</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label class="col-sm-2 control-label">分类</label>
                <div class="col-sm-10">
                    <ul id="tree" class="ztree"></ul>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">名称</label>
                <div class="col-sm-10">
                  <input v-model="course.name" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">讲师</label>
                <div class="col-sm-10">
                  <select v-model="course.teacherId" class="form-control">
                    <option v-for="o in teachers" v-bind:value="o.id">{{o.name}}</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">概述</label>
                <div class="col-sm-10">
                  <input v-model="course.summary" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">时长</label>
                <div class="col-sm-10">
                  <input v-model="course.time" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">价格 (元)</label>
                <div class="col-sm-10">
                  <input v-model="course.price" class="form-control">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">头像</label>
                <div class="col-sm-10">
                  <big-file v-bind:input-id="'image-upload'"
                            v-bind:text="'上传封面'"
                            v-bind:suffixs="['jpg', 'jpeg', 'png']"
                            v-bind:use="FILE_USE.COURSE.key"
                            v-bind:after-upload="afterUpload">
                  </big-file>
                  <div v-show="course.image" class="row">
                    <div class="col-md-6">
                      <img v-bind:src="course.image" class="img-responsive">
                    </div>
                  </div>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">级别</label>
                <div class="col-sm-10">
                  <select v-model="course.level" class="form-control">
                    <option v-for="o in COURSE_LEVEL" v-bind:value="o.key">{{o.value}}</option>
                  </select>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">收费</label>
                <div class="col-sm-10">
                  <select v-model="course.charge" class="form-control">
                    <option v-for="o in COURSE_CHARGE" v-bind:value="o.key">{{o.value}}</option>
                  </select>
                </div>
              </div>

              <div class="form-group">
                <label class="col-sm-2 control-label">状态</label>
                <div class="col-sm-10">
                  <select v-model="course.status" class="form-control">
                    <option v-for="o in COURSE_STATUS" v-bind:value="o.key">{{o.value}}</option>
                  </select>
                </div>
              </div>

            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button v-on:click="save()" type="button" class="btn btn-primary">保存</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <div id="course-sort-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">排序</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label class="control-label col-lg-3">
                  当前排序
                </label>
                <div class="col-lg-9">
                  <input class="form-control" v-model="sort.oldSort" name="oldSort" disabled>
                </div>
              </div>
              <div class="form-group">
                <label class="control-label col-lg-3">
                  新排序
                </label>
                <div class="col-lg-9">
                  <input class="form-control" v-model="sort.newSort" name="newSort">
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-white btn-default btn-round" data-dismiss="modal">
              <i class="ace-icon fa fa-times"></i>
              取消
            </button>
            <button type="button" class="btn btn-white btn-info btn-round" v-on:click="updateSort()">
              <i class="ace-icon fa fa-plus blue"></i>
              更新排序
            </button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
  </div>
</template>

<script>
  import Pagination from "../../components/pagination";
  import BigFile from "../../components/bigFile";

  export default {
    components: {BigFile, Pagination},
    name: "business-course",
    data: function() {
      return {
        course: {},
        tree: {},
        courses: [],
        categorys: [],
        saveContentLabel : "",
        COURSE_LEVEL: COURSE_LEVEL,
        COURSE_CHARGE: COURSE_CHARGE,
        COURSE_STATUS: COURSE_STATUS,
        FILE_USE: FILE_USE,
        sort: {
          id: "",
          oldSort: 0,
          newSort: 0
        },
        teachers: [],
        BUSINESS_COURSE_MANAGEMENT_BROWSER : BUSINESS_COURSE_MANAGEMENT_BROWSER,
        BUSINESS_COURSE_MANAGEMENT_UPDATE : BUSINESS_COURSE_MANAGEMENT_UPDATE,
        BUSINESS_COURSE_MANAGEMENT_DELETE : BUSINESS_COURSE_MANAGEMENT_DELETE
      }
    },
    mounted: function() {
      let _this = this;
      _this.$refs.pagination.size = 5;
      _this.allCategory();
      _this.allTeacher();
      _this.list(1);
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
       * 点击【新增课程】
       */
      add() {
        let _this = this;
        _this.course = {
          sort: _this.$refs.pagination.total + 1
        };
        _this.tree.checkAllNodes(false);
        $("#course-form-modal").modal("show");
      },


      /**
       * 点击【编辑课程】
       */
      edit(course) {
        let _this = this;
        _this.course = $.extend({}, course);
        _this.categoryList(course.id);
        $("#course-form-modal").modal("show");
      },


      /**
       * 课程列表查询
       */
      list(page) {
        let _this = this;
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/list', {
          page: page,
          size: _this.$refs.pagination.size,
        }).then((response)=>{
          Loading.hide();
          let resp = response.data;
          _this.courses = resp.data.list;
          _this.$refs.pagination.render(page, resp.data.total);

        })
      },

      /**
       * 分类列表查询
       */
      allCategory() {
        let _this = this;
        Loading.show();
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/admin/category/all', {
        }).then((response)=>{
          Loading.hide();
          _this.categorys = response.data.data;
          _this.initTree();
        })
      },


      /**
       * 初始化ZTree
       */
      initTree() {
        let _this = this;
        let setting = {
          check: {
            enable: true
          },
          data: {
            simpleData: {
              idKey: "id",
              pIdKey: "parent",
              rootPId: "00000000",
              enable: true
            }
          }
        };
        let zNodes = _this.categorys;
        _this.tree = $.fn.zTree.init($("#tree"), setting, zNodes);
      },


      /**
       * 点击【保存】
       */
      save() {
        let _this = this;
        // 保存校验
        if (1 !== 1
          || !Validator.require(_this.course.name, "名称")
          || !Validator.length(_this.course.name, "名称", 1, 50)
          || !Validator.require(_this.course.summary, "概述")
          || !Validator.length(_this.course.summary, "概述", 1, 2000)
        ) {
          return;
        }

        let categorys = _this.tree.getCheckedNodes();
        if (Tool.isEmpty(categorys)) {
          Toast.warning("请选择分类！");
          return;
        }

        _this.course.categorys = categorys;
        console.log(_this.course.teacherId);
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/save', _this.course).then((response)=>{
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            $("#course-form-modal").modal("hide");
            _this.list(1);
            Toast.success("保存成功！");
          } else {
            Toast.warning(resp.message)
          }
        })
      },


      /**
       * 点击【删除课程】
       */
      del(id) {
        let _this = this;
        Confirm.show("删除课程后不可恢复，确认删除？", function () {
          Loading.show();
          _this.$ajax.delete(process.env.VUE_APP_SERVER + '/business/admin/course/delete/' + id).then((response)=>{
            Loading.hide();
            let resp = response.data;
            if (resp.success) {
              _this.list(1);
              Toast.success("删除成功！");
            }
          })
        });
      },


      /**
       * 点击【大章页面】
       */
      toChapter(course) {
        let _this = this;
        SessionStorage.set(SESSION_KEY_COURSE, course);
        _this.$router.push("/business/chapter");
      },


      /**
       * 点击【课程内容】
       */
      toContent(course) {
        let _this = this;
        SessionStorage.set(SESSION_KEY_COURSE, course);
        _this.$router.push("/business/content");
      },


      /**
       * 根据课程 ID 查找课程关联分类的列表
       */
      categoryList(courseId) {
        let _this = this;
        Loading.show();
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/admin/course/get-course-category-list/' + courseId, {
        }).then((response)=>{
          Loading.hide();
          let categorys = response.data.data;

          // 勾选查询到的分类
          _this.tree.checkAllNodes(false);
          for (let i = 0; i < categorys.length; i++) {
            let node = _this.tree.getNodeByParam("id", categorys[i].categoryId);
            _this.tree.checkNode(node, true);
          }
        })
      },


      /**
       * 打开排序模态框
       */
      openSortModal(course) {
        let _this = this;
        _this.sort = {
          id: course.id,
          oldSort: course.sort,
          newSort: course.sort
        };
        $("#course-sort-modal").modal("show");
      },


      /**
       * 排序
       */
      updateSort() {
        let _this = this;
        if (_this.sort.newSort === _this.sort.oldSort) {
          Toast.warning("排序没有变化");
          return;
        }
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/course/sort", _this.sort).then((res) => {
          let response = res.data;

          if (response.success) {
            Toast.success("更新排序成功");
            $("#course-sort-modal").modal("hide");
            _this.list(1);
          } else {
            Toast.error("更新排序失败");
          }
        });
      },


      /**
       * 查询所有讲师
       */
      allTeacher() {
        let _this = this;
        Loading.show();
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/admin/teacher/all').then((response)=>{
          Loading.hide();
          let resp = response.data;
          _this.teachers = resp.data;
        })
      },


      /**
       * 上传文件回调函数
       */
      afterUpload(resp) {
        let _this = this;
        console.log(resp.data.path)
        _this.course.image = resp.data.path;
      }

    }
  }
</script>

<style scoped>
  .caption h3 {
    font-size: 20px;
  }

  @media (max-width: 1199px) {
    .caption h3 {
      font-size: 16px;
    }
  }
</style>