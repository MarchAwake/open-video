<template>
    <div class="main-container">
        <div class="main-content">
            <div class="row">
                <div class="col-sm-10 col-sm-offset-1">
                    <div class="login-container">
                        <div class="center">
                            <h1>
                                <i class="ace-icon fa fa-leaf green"></i>
                                <span class="red">管理员登录</span>
                            </h1>
                        </div>

                        <div class="space-6"></div>

                        <div class="position-relative">
                            <div id="login-box" class="login-box visible widget-box no-border">
                                <div class="widget-body">
                                    <div class="widget-main">
                                        <h4 class="header blue lighter bigger">
                                            <i class="ace-icon fa fa-coffee green"></i>
                                            请输入你的账户信息
                                        </h4>

                                        <div class="space-6"></div>

                                        <form>
                                            <fieldset>
                                                <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input v-model="user.loginName" type="text" class="form-control" placeholder="用户名" />
															<i class="ace-icon fa fa-user"></i>
														</span>
                                                </label>

                                                <label class="block clearfix">
														<span class="block input-icon input-icon-right">
															<input v-model="user.password" type="password" class="form-control" placeholder="密码" />
															<i class="ace-icon fa fa-lock"></i>
														</span>
                                                </label>

                                                <label class="block clearfix">
                                                      <span class="block input-icon input-icon-right">
                                                        <div class="input-group">
                                                          <input v-model="user.imageCode" type="text" class="form-control" placeholder="验证码">
                                                          <span class="input-group-addon" id="basic-addon2">
                                                            <img v-on:click="loadImageCode()" id="image-code" alt="验证码"/>
                                                          </span>
                                                        </div>
                                                      </span>
                                                </label>

                                                <div class="space"></div>

                                                <div class="clearfix">
                                                    <label class="inline">
                                                        <input v-model="remember" type="checkbox" class="ace" />
                                                        <span class="lbl"> 记住密码</span>
                                                    </label>

                                                    <button type="button"
                                                            class="width-35 pull-right btn btn-sm btn-primary"
                                                    v-on:click="login()">
                                                        <i class="ace-icon fa fa-key"></i>
                                                        <span class="bigger-110">登录</span>
                                                    </button>
                                                </div>

                                                <div class="space-4"></div>
                                            </fieldset>
                                        </form>
                                    </div><!-- /.widget-main -->
                                </div><!-- /.widget-body -->
                            </div><!-- /.login-box -->
                        </div><!-- /.position-relative -->
                    </div>
                </div><!-- /.col -->
            </div><!-- /.row -->
        </div><!-- /.main-content -->
    </div>
</template>

<script>

    export default {
        name: 'login',
        data: function() {
           return{
               user: {},
               remember: true,
               imageCodeToken: ""
           }
        },

        mounted: function() {
            $("body").removeClass('no-skin');
            $('body').attr('class', 'login-layout light-login');
            let _this = this;
            let rememberUser = LocalStorage.get(LOCAL_KEY_REMEMBER_USER);
            if (rememberUser) {
                _this.user = rememberUser;
            }

            _this.loadImageCode();
        },

        methods: {
            /**
             * 登录
             */
            login() {
                let _this = this;
                if (1 !== 1
                    || !Validator.require(_this.user.loginName, "登录名")
                    || !Validator.require(_this.user.password, "密码")
                ) {
                    return;
                }
                let md5 = hex_md5(_this.user.password);
                let rememberUser = LocalStorage.get(LOCAL_KEY_REMEMBER_USER) || {};
                if (md5 !== rememberUser.md5) {
                    _this.user.password = hex_md5(_this.user.password + KEY);
                }
                _this.user.imageCodeToken = _this.imageCodeToken;
                Loading.show();
                _this.$ajax.post(process.env.VUE_APP_SERVER + '/system/admin/user/login', _this.user).then((response)=>{
                    Loading.hide();
                    let resp = response.data;
                    console.log(response);
                    if (resp.success) {
                        Tool.setLoginUser(resp.data);
                        console.log("loginUser" + Tool.getLoginUser());
                        _this.$router.push("/welcome");
                        if (_this.remember) {
                            let md5 = hex_md5(_this.user.password);
                            LocalStorage.set(LOCAL_KEY_REMEMBER_USER, {
                                loginName: _this.user.loginName,
                                password: _this.user.password,
                                md5 : md5
                            });
                        } else {
                            LocalStorage.remove(LOCAL_KEY_REMEMBER_USER);
                        }
                    } else {
                        Toast.warning(resp.message);
                        _this.user.imageCode = "";
                        _this.loadImageCode();
                    }
                })

            },

            /**
             * 获取验证码
             */
            loadImageCode() {
                let _this = this;
                _this.imageCodeToken = Tool.uuid(8);
                $('#image-code').attr('src', process.env.VUE_APP_SERVER + '/system/admin/kaptcha/image-code/' + _this.imageCodeToken);
            }
        }
    }
</script>

