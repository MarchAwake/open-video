<template>
  <div>
    <h4 class="lighter">
      <i class="ace-icon fa fa-hand-o-right icon-animated-hand-pointer blue"></i>
      <router-link to="/business/course" class="pink"> {{course.name}} </router-link>
    </h4>
    <hr>

    <big-file v-show="hasResource(BUSINESS_COURSE_MANAGEMENT_UPDATE)" v-bind:input-id="'content-file-upload'"
          v-bind:text="'上传文件'"
          v-bind:suffixs="['jpg', 'jpeg', 'png', 'txt', 'pdf']"
          v-bind:use="FILE_USE.COURSE.key"
          v-bind:after-upload="afterUploadContentFile">
    </big-file>
    <br>
    <table id="file-table" class="table  table-bordered table-hover">
      <thead>
      <tr>
        <th>名称</th>
        <th>地址</th>
        <th>大小</th>
        <th>操作</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="(f, i) in files" v-bind:key="f.id">
        <td>{{f.name}}</td>
        <td>{{f.url}}</td>
        <td>{{f.size | formatFileSize}}</td>
        <td v-show="hasResource(BUSINESS_COURSE_MANAGEMENT_DELETE)">
          <button v-on:click="delFile(f)" class="btn btn-white btn-xs btn-warning btn-round">
            <i class="ace-icon fa fa-times red2"></i>
            删除
          </button>
        </td>
      </tr>
      </tbody>
    </table>
    <form class="form-horizontal">
      <div class="form-group">
        <div class="col-lg-12">
          {{saveContentLabel}}
        </div>
      </div>
      <div class="form-group">
        <div class="col-lg-12">
          <div id="content"></div>
        </div>
      </div>
      <div class="form-group">
        <div class="col-lg-12">
          {{saveContentLabel}}
        </div>
      </div>
    </form>
    <p>
      <button v-show="hasResource(BUSINESS_COURSE_MANAGEMENT_UPDATE)" type="button" class="btn btn-white btn-info btn-round" v-on:click="saveContent()">
        <i class="ace-icon fa fa-plus blue"></i>
        保存
      </button>&nbsp;
      <router-link to="/business/course" type="button" class="btn btn-white btn-default btn-round" data-dismiss="modal">
        <i class="ace-icon fa fa-times"></i>
        返回课程
      </router-link>
    </p>
  </div>
</template>

<script>
  import BigFile from "../../components/bigFile";
  export default {
    components: {BigFile},
    name: "business-course-content",
    data: function() {
      return {
        course: {},
        FILE_USE: FILE_USE,
        saveContentLabel: "",
        files: [],
        saveContentInterval: {},
        BUSINESS_COURSE_MANAGEMENT_BROWSER : BUSINESS_COURSE_MANAGEMENT_BROWSER,
        BUSINESS_COURSE_MANAGEMENT_UPDATE : BUSINESS_COURSE_MANAGEMENT_UPDATE,
        BUSINESS_COURSE_MANAGEMENT_DELETE : BUSINESS_COURSE_MANAGEMENT_DELETE
      }
    },
    mounted: function() {
      let _this = this;

      let course = SessionStorage.get(SESSION_KEY_COURSE) || {};
      if (Tool.isEmpty(course)) {
        _this.$router.push("/welcome");
      }
      _this.course = course;

      _this.init();

      // sidebar激活样式方法一
      this.$parent.activeSidebar("business-course-sidebar");

    },
    destroyed: function() {
      let _this = this;
      console.log("组件销毁");
      clearInterval(_this.saveContentInterval);
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
       * 打开内容编辑框
       */
      init() {
        let _this = this;
        let course = _this.course;
        let id = course.id;
        $("#content").summernote({
          focus: true,
          height: 300
        });

        // 先清空历史文本
        $("#content").summernote('code', '');
        _this.saveContentLabel = "";

        // 加载内容文件列表
        _this.listContentFiles();

        Loading.show();
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/admin/course/get-course-content/' + id).then((response)=>{
          Loading.hide();
          let resp = response.data;

          if (resp.success) {
            if (resp.data) {
              $("#content").summernote('code', resp.data.content);
            }

          } else {
            Toast.warning(resp.message);
          }
        });
      },

      /**
       * 保存内容
       */
      saveContent () {
        let _this = this;
        let content = $("#content").summernote("code");
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course/save-course-content', {
          id: _this.course.id,
          content: content
        }).then((response)=>{
          Loading.hide();
          let resp = response.data;
          if (resp.success) {
            let now = Tool.dateFormat("yyyy-MM-dd hh:mm:ss");
            _this.saveContentLabel = "最后保存时间：" + now;
          } else {
            Toast.warning(resp.message);
          }
        });
      },

      /**
       * 加载内容文件列表
       */
      listContentFiles() {
        let _this = this;
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/admin/course-content-file/list/' + _this.course.id).then((response)=>{
          let resp = response.data;
          if (resp.success) {
            _this.files = resp.data;
          }
        });
      },

      /**
       * 上传课程内容文件后，保存课程内容文件记录
       */
      afterUploadContentFile(response) {
        let _this = this;
        console.log("开始保存文件记录");
        let file = response.data;
        file.courseId = _this.course.id;
        file.url = file.path;
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/course-content-file/save', file).then((response)=>{
          let resp = response.data;
          if (resp.success) {
            Toast.success("上传文件成功");
            _this.files.push(resp.data);
          }
        });

      },

      /**
       * 删除课程内容文件
       */
      delFile(f) {
        let _this = this;
        Confirm.show("删除课程后不可恢复，确认删除？", function () {
          _this.$ajax.delete(process.env.VUE_APP_SERVER + '/business/admin/course-content-file/delete/' + f.id).then((response)=>{
            let resp = response.data;
            if (resp.success) {
              Toast.success("删除文件成功");
              Tool.removeObj(_this.files, f);
            }
          });
        });
      },
    }
  }
</script>