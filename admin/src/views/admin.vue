<template>
    <div>
        <div id="navbar" class="navbar navbar-default          ace-save-state">
            <div class="navbar-container ace-save-state" id="navbar-container">
                <button type="button" class=" navbar-toggle menu-toggler pull-left" id="menu-toggler" data-target="#sidebar">
                    <span class="sr-only"></span>

                    <span class="icon-bar"></span>

                    <span class="icon-bar"></span>

                    <span class="icon-bar"></span>
                </button>

                <div class="navbar-header pull-left">
                    <a href="#" class="navbar-brand">
                        <small>
                            <i class="fa fa-leaf"></i>
                            开源视频
                        </small>
                    </a>
                    <div class="  navbar-buttons navbar-header pull-right float-lg-right" >
                        <ul class="nav ace-nav">
                            <li class="light-blue dropdown-modal">
                                <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                                    <img class="nav-user-photo" src="../../public/ace/assets/images/avatars/user.jpg" alt="Jason's Photo" />
                                </a>

                                <ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                                    <li>
                                        <a href="#">
                                            <i class="ace-icon fa fa-cog"></i>
                                            系统设置
                                        </a>
                                    </li>

                                    <li>
                                        <a href="#">
                                            <i class="ace-icon fa fa-user"></i>
                                            个人信息
                                        </a>
                                    </li>

                                    <li class="divider"></li>

                                    <li>
                                        <a v-on:click="logout()" href="#">
                                            <i class="ace-icon fa fa-power-off"></i>
                                            退出登录
                                        </a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
            </div><!-- /.navbar-container -->
        </div>

        <div class="main-container ace-save-state" id="main-container">
            <div id="sidebar" class="sidebar     responsive      ace-save-state">
                <ul class="nav nav-list">
                    <li id="welcome-sidebar">
                        <router-link to="/welcome">
                            <i class="menu-icon fa fa-tachometer"></i>
                            <span class="menu-text"> 欢迎 {{loginUser.nickname}}</span>
                        </router-link>

                        <b class="arrow"></b>
                    </li>

                    <li v-show="hasResource(SYSTEM_MANAGEMENT)">
                        <a href="#" class="dropdown-toggle">
                            <i class="menu-icon fa fa-list"></i>
                            <span class="menu-text"> 系统管理 </span>

                            <b class="arrow fa fa-angle-down"></b>
                        </a>

                        <ul class="submenu">
                            <li v-show="hasResource(SYSTEM_USER_MANAGEMENT)" id="system-user-sidebar">
                                <router-link to="/system/user">
                                    <i class="menu-icon fa fa-caret-right"></i>
                                    用户管理
                                </router-link>
                                <b class="arrow"></b>
                            </li>

                            <li v-show="hasResource(SYSTEM_RESOURCE_MANAGEMENT)"  id="system-resource-sidebar">
                                <router-link to="/system/resource">
                                    <i class="menu-icon fa fa-caret-right"></i>
                                    资源管理
                                </router-link>
                                <b class="arrow"></b>
                            </li>

                            <li v-show="hasResource(SYSTEM_ROLE_MANAGEMENT)" id="system-role-sidebar">
                                <router-link to="/system/role">
                                    <i class="menu-icon fa fa-caret-right"></i>
                                    角色管理
                                </router-link>
                                <b class="arrow"></b>
                            </li>
                        </ul>
                    </li>

                    <li v-show="hasResource(BUSINESS_MANAGEMENT)">
                        <a href="#" class="dropdown-toggle">
                            <i class="menu-icon fa fa-list"></i>
                            <span class="menu-text"> 业务管理 </span>

                            <b class="arrow fa fa-angle-down"></b>
                        </a>

                        <ul class="submenu">
                            <li v-show="hasResource(BUSINESS_CATEGORY_MANAGEMENT)" id="business-category-sidebar">
                                <router-link to="/business/category">
                                    <i class="menu-icon fa fa-caret-right"></i>
                                    分类管理
                                </router-link>

                                <b class="arrow"></b>
                            </li>
                            <li v-show="hasResource(BUSINESS_COURSE_MANAGEMENT)" id="business-course-sidebar">
                                <router-link to="/business/course">
                                    <i class="menu-icon fa fa-caret-right"></i>
                                    课程管理
                                </router-link>

                                <b class="arrow"></b>
                            </li>
                            <li v-show="hasResource(BUSINESS_TEACHER_MANAGEMENT)" id="business-teacher-sidebar">
                                <router-link to="/business/teacher">
                                    <i class="menu-icon fa fa-caret-right"></i>
                                    教师管理
                                </router-link>

                                <b class="arrow"></b>
                            </li>
                            <li  v-show="hasResource(BUSINESS_MEMBER_MANAGEMENT)" id="business-member-sidebar">
                                <router-link to="/business/member">
                                    <i class="menu-icon fa fa-caret-right"></i>
                                    会员管理
                                </router-link>
                                <b class="arrow"></b>
                            </li>
                            <li v-show="hasResource(BUSINESS_EMAILBOX_MANAGEMENT)" id="business-emailbox-sidebar">
                                <router-link to="/business/emailbox">
                                    <i class="menu-icon fa fa-caret-right"></i>
                                    验证记录
                                </router-link>

                                <b class="arrow"></b>
                            </li>
                        </ul>
                    </li>
                    <li v-show="hasResource(FILE_MANAGEMENT)">
                        <a href="#" class="dropdown-toggle">
                            <i class="menu-icon fa fa-list"></i>
                            <span class="menu-text"> 文件管理 </span>

                            <b class="arrow fa fa-angle-down"></b>
                        </a>

                        <ul class="submenu">
                            <li v-show="hasResource(FILE_FILE_MANAGEMENT)" id="file-manage-sidebar">
                                <router-link to="/file/file">
                                    <i class="menu-icon fa fa-caret-right"></i>
                                    文件
                                </router-link>

                                <b class="arrow"></b>
                            </li>
                        </ul>
                    </li>
                </ul><!-- /.nav-list -->

                <div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
                    <i id="sidebar-toggle-icon" class="ace-icon fa fa-angle-double-left ace-save-state" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
                </div>
            </div>

            <div class="main-content">
                <div class="main-content-inner">

                    <div class="page-content">

                        <div class="row">
                            <div class="col-xs-12">
                                <!-- PAGE CONTENT BEGINS -->
                                <router-view/>
                                <!-- PAGE CONTENT ENDS -->
                            </div><!-- /.col -->
                        </div><!-- /.row -->
                    </div><!-- /.page-content -->
                </div>
            </div><!-- /.main-content -->

            <div class="footer">
                <div class="footer-inner">
                    <div class="footer-content">
						<span class="bigger-120">
							<span class="blue bolder">开源视频</span>
							开源视频 &copy; 2020-2021
						</span>
                    </div>
                </div>
            </div>

            <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
                <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
            </a>
        </div><!-- /.main-container -->
    </div>
</template>
<script>

    export default {
        name: 'admin',
        data: function() {
           return {
               loginUser: {},
               SYSTEM_MANAGEMENT :SYSTEM_MANAGEMENT,
               SYSTEM_USER_MANAGEMENT : SYSTEM_USER_MANAGEMENT,
               SYSTEM_RESOURCE_MANAGEMENT : SYSTEM_RESOURCE_MANAGEMENT,
               SYSTEM_ROLE_MANAGEMENT : SYSTEM_ROLE_MANAGEMENT,

               BUSINESS_MANAGEMENT : BUSINESS_MANAGEMENT,
               BUSINESS_CATEGORY_MANAGEMENT : BUSINESS_CATEGORY_MANAGEMENT,
               BUSINESS_COURSE_MANAGEMENT : BUSINESS_COURSE_MANAGEMENT,
               BUSINESS_TEACHER_MANAGEMENT : BUSINESS_TEACHER_MANAGEMENT,
               BUSINESS_MEMBER_MANAGEMENT : BUSINESS_MEMBER_MANAGEMENT,
               BUSINESS_EMAILBOX_MANAGEMENT : BUSINESS_EMAILBOX_MANAGEMENT,

               FILE_MANAGEMENT : FILE_MANAGEMENT,
               FILE_FILE_MANAGEMENT : FILE_FILE_MANAGEMENT,
           }
        },
        //先清除样式在设置样式
        mounted: function() {
            let _this = this;
            let body =  $("body");
            body.removeClass("login-layout light-login");
            body.attr("class", "no-skin");
            _this.activeSidebar(_this.$route.name.replace("/", "-") + "-sidebar");
            _this.loginUser = Tool.getLoginUser();
        },
        // 当前父级路由下监听子路由变化
        watch: {
            $route: {
                handler:function(val, oldVal){
                    // sidebar激活样式方法二
                    console.log("---->页面跳转：", val, oldVal);
                    let _this = this;
                    _this.$nextTick(function(){  //页面加载完成后执行
                        _this.activeSidebar(_this.$route.name.replace("/", "-") + "-sidebar");
                    })
                }
            }
        },

       methods: {

           /**
            * 查找是否有权限
            * @param router
            */
           hasResourceRouter(router) {
               let _this = this;
               let resources = Tool.getLoginUser().resources;
               if (Tool.isEmpty(resources)) {
                   return false;
               }
               for (let i = 0; i < resources.length; i++) {
                   if (router === resources[i].page) {
                       return true;
                   }
               }
               return false;
           },

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
            * 菜单激活样式，id 是当前菜单点击的菜单 id
            * @param id
            */
           activeSidebar: function(id) {
               // 兄弟菜单去掉active样式，自身增加active样式
               let ele = $("#" + id);
               ele.siblings().removeClass("active");
               ele.siblings().find("li").removeClass("active");
               ele.addClass("active");

               // 如果有父菜单，则父菜单的兄弟菜单去掉 open active, 父菜单增加 open active
               let parentLi = ele.parent("li");
               if (parentLi) {
                   parentLi.siblings().removeClass("open active");
                   parentLi.siblings().find("li").removeClass("active");
                   parentLi.addClass("open active");
               }
           },

           logout() {
               let _this = this;
               Loading.show();
               _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/logout/' + _this.loginUser.token).then((response)=>{
                   Loading.hide();
                   let resp = response.data;
                   console.log(resp.data);
                   if (resp.success) {
                       Tool.setLoginUser(null);
                       _this.$router.push("/login");
                   } else {
                       Toast.warning(resp.message)
                   }
               })


           },
       }

    }
</script>