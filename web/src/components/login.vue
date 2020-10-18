<template>
  <div id="login-modal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-login" role="document">
      <div class="modal-content">
        <div class="modal-body">

          <div class="login-div" v-show="MODAL_STATUS === STATUS_LOGIN">
            <h3>登&nbsp;&nbsp;录</h3>
            <div class="form-group">
              <input v-model="member.email" class="form-control" placeholder="邮箱">
            </div>
            <div class="form-group">
              <input class="form-control" type="password" placeholder="密码" v-model="member.password">
            </div>
            <div class="form-group">
              <div class="input-group">
                <input id="image-code-input" class="form-control" type="text" placeholder="验证码"
                       v-model="member.imageCode">
                <div class="input-group-addon" id="image-code-addon">
                  <img id="image-code" alt="验证码" v-on:click="loadImageCode()"/>
                </div>
              </div>
            </div>
            <div class="form-group">
              <button v-on:click="login()" class="btn btn-primary btn-block submit-button">
                登&nbsp;&nbsp;录
              </button>
            </div>
            <div class="form-group">
              <div class="checkbox">
                <label>
                  <input type="checkbox" class="remember" v-model="remember"> 记住密码
                </label>
                <div class="pull-right">
                  <a href="javascript:;" v-on:click="toForgetDiv()">忘记密码</a>&nbsp;
                  <a href="javascript:;" v-on:click="toRegisterDiv()">我要注册</a>
                </div>
              </div>
            </div>
            <div class="form-group to-register-div">
            </div>
          </div>


          <div class="register-div" v-show="MODAL_STATUS === STATUS_REGISTER">
            <h3>注&nbsp;&nbsp;册</h3>
            <div class="form-group">
              <input v-on:blur="onRegisterEmailBlur()"
                     v-bind:class="registerEmailValidateClass"
                     id="register-email" v-model="memberRegister.email"
                     class="form-control" placeholder="邮箱">
              <span v-show="registerEmailValidate === false" class="text-danger">请输入正确的邮箱</span>
            </div>
            <div class="form-group">
              <div class="input-group">
                <input v-on:blur="onRegisterEmailCodeBlur()"
                       v-bind:class="registerEmailCodeValidateClass"
                       id="register-email-code" class="form-control"
                       placeholder="邮箱验证码" v-model="memberRegister.emailCode">
                <div class="input-group-append">
                  <button class="btn btn-outline-secondary" id="register-send-code-btn"
                          v-on:click="sendEmailForRegister()">发送验证码
                  </button>
                </div>
              </div>
              <span v-show="registerEmailCodeValidate === false" class="text-danger">请输入邮箱6位验证码</span>
            </div>
            <div class="form-group">
              <input v-on:blur="onRegisterNameBlur()"
                     v-bind:class="registerNameValidateClass"
                     id="register-name" v-model="memberRegister.name"
                     class="form-control" placeholder="昵称">
              <span v-show="registerNameValidate === false" class="text-danger">昵称2到20位中文，字母，数字，下划线组合</span>
            </div>
            <div class="form-group">
              <input v-on:blur="onRegisterPasswordBlur()"
                     v-bind:class="registerPasswordValidateClass"
                     id="register-password" v-model="memberRegister.passwordOriginal"
                     class="form-control" placeholder="密码" type="password">
              <span v-show="registerPasswordValidate === false" class="text-danger">密码最少6位，包含至少1字母和1个数字</span>
            </div>
            <div class="form-group">
              <input v-on:blur="onRegisterConfirmPasswordBlur()"
                     v-bind:class="registerConfirmPasswordValidateClass"
                     id="register-confirm-password" v-model="memberRegister.confirm"
                     class="form-control" placeholder="确认密码"
                     name="memberRegisterConfirm" type="password">
              <span v-show="registerConfirmPasswordValidate === false" class="text-danger">确认密码和密码一致</span>
            </div>
            <div class="form-group">
              <button class="btn btn-primary btn-block submit-button" v-on:click="register()">
                注&nbsp;&nbsp;册
              </button>
            </div>
            <div class="form-group to-login-div">
              <a href="javascript:;" v-on:click="toLoginDiv()">我要登录</a>
            </div>
          </div>


          <div class="forget-div" v-show="MODAL_STATUS === STATUS_FORGET">
            <h3>忘记密码</h3>
            <div class="form-group">
              <input v-on:blur="onForgetEmailBlur()"
                     v-bind:class="forgetEmailValidateClass"
                     id="forget-email" v-model="memberForget.email"
                     class="form-control" placeholder="邮箱">
              <span v-show="forgetEmailValidate === false" class="text-danger">请输入正确的邮箱</span>
            </div>
            <div class="form-group">
              <div class="input-group">
                <input v-on:blur="onForgetEmailCodeBlur()"
                       v-bind:class="forgetEmailCodeValidateClass"
                       id="forget-email-code" class="form-control"
                       placeholder="邮箱验证码" v-model="memberForget.emailCode">
                <div class="input-group-append">
                  <button v-on:click="sendEmailForForget()"
                          class="btn btn-outline-secondary" id="forget-send-code-btn">
                    发送验证码
                  </button>
                </div>
              </div>
              <span v-show="forgetEmailCodeValidate === false" class="text-danger">请输入邮箱6位验证码</span>
            </div>
            <div class="form-group">
              <input v-on:blur="onForgetPasswordBlur()"
                     v-bind:class="forgetPasswordValidateClass"
                     id="forget-password" v-model="memberForget.passwordOriginal"
                     class="form-control" placeholder="密码" type="password">
              <span v-show="forgetPasswordValidate === false" class="text-danger">密码最少6位，包含至少1字母和1个数字</span>
            </div>

            <div class="form-group">
              <input v-on:blur="onForgetConfirmPasswordBlur()"
                     v-bind:class="forgetConfirmPasswordValidateClass"
                     id="forget-confirm-password" v-model="memberForget.confirm"
                     class="form-control" placeholder="确认密码" type="password">
              <span v-show="forgetConfirmPasswordValidate === false" class="text-danger">确认密码和密码一致</span>
            </div>
            <div class="form-group">
              <button v-on:click="resetPassword()" class="btn btn-primary btn-block submit-button">
                重&nbsp;&nbsp;置
              </button>
            </div>
            <div class="form-group to-login-div">
              <a href="javascript:;" v-on:click="toLoginDiv()">我要登录</a>
            </div>
          </div>
        </div>
      </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
  </div><!-- /.modal -->
</template>

<script>

  export default {
    name: 'the-login',
    data: function () {
      return {
        // 模态框内容切换：登录、注册、忘记密码
        STATUS_LOGIN: "STATUS_LOGIN",
        STATUS_REGISTER: "STATUS_REGISTER",
        STATUS_FORGET: "STATUS_FORGET",
        MODAL_STATUS: "",

        member: {},
        memberForget: {},
        memberRegister: {},

        remember: true, // 记住密码
        imageCodeToken: "",

        // 注册框显示错误信息
        registerEmailValidate: null,
        registerEmailCodeValidate: null,
        registerPasswordValidate: null,
        registerNameValidate: null,
        registerConfirmPasswordValidate: null,

        // 忘记密码框显示错误信息
        forgetEmailValidate: null,
        forgetEmailCodeValidate: null,
        forgetPasswordValidate: null,
        forgetConfirmPasswordValidate: null,
      }
    },
    computed: {
      registerEmailValidateClass: function () {
        return {
          'border-success': this.registerEmailValidate === true,
          'border-danger': this.registerEmailValidate === false,
        }
      },
      registerEmailCodeValidateClass: function () {
        return {
          'border-success': this.registerEmailCodeValidate === true,
          'border-danger': this.registerEmailCodeValidate === false,
        }
      },
      registerPasswordValidateClass: function () {
        return {
          'border-success': this.registerPasswordValidate === true,
          'border-danger': this.registerPasswordValidate === false,
        }
      },
      registerNameValidateClass: function () {
        return {
          'border-success': this.registerNameValidate === true,
          'border-danger': this.registerNameValidate === false,
        }
      },
      registerConfirmPasswordValidateClass: function () {
        return {
          'border-success': this.registerConfirmPasswordValidate === true,
          'border-danger': this.registerConfirmPasswordValidate === false,
        }
      },
      forgetEmailValidateClass: function () {
        return {
          'border-success': this.forgetEmailValidate === true,
          'border-danger': this.forgetEmailValidate === false,
        }
      },
      forgetEmailCodeValidateClass: function () {
        return {
          'border-success': this.forgetEmailCodeValidate === true,
          'border-danger': this.forgetEmailCodeValidate === false,
        }
      },
      forgetPasswordValidateClass: function () {
        return {
          'border-success': this.forgetPasswordValidate === true,
          'border-danger': this.forgetPasswordValidate === false,
        }
      },
      forgetConfirmPasswordValidateClass: function () {
        return {
          'border-success': this.forgetConfirmPasswordValidate === true,
          'border-danger': this.forgetConfirmPasswordValidate === false,
        }
      },
    },

    mounted() {
      let _this = this;
      _this.toLoginDiv();
    },
    methods: {

      /**
       * 打开登录注册窗口
       */
      openLoginModal() {
        let _this = this;
        $("#login-modal").modal("show");
      },

      //---------------登录框、注册框、忘记密码框切换-----------------
      toLoginDiv() {
        let _this = this;

        // 从缓存中获取记住的用户名密码，如果获取不到，说明上一次没有勾选“记住我”
        let rememberMember = LocalStorage.get(LOCAL_KEY_REMEMBER_MEMBER);
        if (rememberMember) {
          _this.member = rememberMember;
        }

        // 显示登录框时就刷新一次验证码图片
        _this.loadImageCode();

        _this.MODAL_STATUS = _this.STATUS_LOGIN
      },
      toRegisterDiv() {
        let _this = this;
        _this.MODAL_STATUS = _this.STATUS_REGISTER
      },
      toForgetDiv() {
        let _this = this;
        _this.MODAL_STATUS = _this.STATUS_FORGET
      },

      register() {
        let _this = this;

        // 提交之前，先校验所有输入框
        // 注意：当有一个文本框校验为false时，其它不校验
        let validateResult = _this.onRegisterEmailBlur() &&
          _this.onRegisterEmailCodeBlur() &&
          _this.onRegisterNameBlur() &&
          _this.onRegisterPasswordBlur() &&
          _this.onRegisterConfirmPasswordBlur();
        if (!validateResult) {
          return;
        }

        _this.memberRegister.password = hex_md5(_this.memberRegister.passwordOriginal + KEY);

        // 调服务端注册接口
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/member/register', _this.memberRegister).then((response) => {
          let resp = response.data;
          if (resp.success) {
            Toast.success("注册成功");
            $("#login-modal").modal("hide");
            _this.MODAL_STATUS = "";

          } else {
            Toast.warning(resp.message);
          }
        })
      },


      //---------------登录框-----------------
      login () {
        let _this = this;

        // 如果密码是从缓存带出来的，则不需要重新加密
        let md5 = hex_md5(_this.member.password);
        let rememberMember = LocalStorage.get(LOCAL_KEY_REMEMBER_MEMBER) || {};
        if (md5 !== rememberMember.md5) {
          _this.member.password = hex_md5(_this.member.password + KEY);
        }

        _this.member.imageCodeToken = _this.imageCodeToken;

        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/member/login', _this.member).then((response)=>{
          let resp = response.data;
          if (resp.success) {
            let loginMember = resp.data;
            Tool.setLoginMember(resp.data);

            // 判断“记住我”
            if (_this.remember) {
              // 如果勾选记住我，则将用户名密码保存到本地缓存
              // 这里保存密码密文，并保存密文md5，用于检测密码是否被重新输入过
              let md5 = hex_md5(_this.member.password);
              LocalStorage.set(LOCAL_KEY_REMEMBER_MEMBER, {
                email: loginMember.email,
                password: _this.member.password,
                md5: md5
              });
            } else {
              // 没有勾选“记住我”时，要把本地缓存清空，否则下次显示登录框时会自动显示用户名密码
              LocalStorage.set(LOCAL_KEY_REMEMBER_MEMBER, null);
            }

            // 登录成功
            _this.$parent.setLoginMember(loginMember);
            $("#login-modal").modal("hide");


          } else {
            Toast.warning(resp.message);
            _this.member.password = "";
            _this.loadImageCode();
          }
        });
      },



      /**
       * 加载图形验证码
       */
      loadImageCode: function () {
        let _this = this;
        _this.imageCodeToken = Tool.uuid(8);
        $('#image-code').attr('src', process.env.VUE_APP_SERVER + '/business/web/kaptcha/image-code/' + _this.imageCodeToken);
      },




      /**
       * 发送注册邮件
       */
      sendEmailForRegister() {
        let _this = this;

        if (!_this.onRegisterEmailBlur()) {
          return false;
        }

        let emailbox = {
          email: _this.memberRegister.email,
          use: EMAILBOX_USE.REGISTER.key
        };

        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/web/member/check-email-exist/' + _this.memberRegister.email).then((res)=>{
          let response = res.data;
          if (response.data) {
            Toast.warning("邮箱已被注册");
          } else {
            // 调服务端发送验证接口
            _this.sendEmailboxCode(emailbox, "register-send-code-btn");
          }
        })
      },



      /**
       * 发送邮件
       */
      sendEmailboxCode(emailbox, btnId) {
        let _this = this;

        // 调服务端发邮件接口
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/emailbox/send', emailbox).then((res)=> {
          let response = res.data;
          if (response.success) {
            Toast.success("邮件已发送");

            // 开始倒计时
            _this.countdown = 60;
            _this.setTime(btnId);
          } else {
            Toast.warning(response.message);
          }
        })
      },



      /**
       * 倒计时
       * @param btnId
       */
      setTime(btnId) {
        let _this = this;
        let btn = $("#" + btnId);
        if (_this.countdown === 0) {
          btn.removeAttr("disabled");
          btn.text("获取验证码");
          return;
        } else {
          btn.attr("disabled", true);
          btn.text("重新发送(" + _this.countdown + ")");
          _this.countdown--;
        }
        setTimeout(function () {
          _this.setTime(btnId);
        }, 1000);
      },



      /**
       * 发送忘记密码邮件
       */
      sendEmailForForget() {
        let _this = this;
        if (!_this.onForgetEmailBlur()) {
          return false;
        }
        let emailbox = {
          email: _this.memberForget.email,
          use: EMAILBOX_USE.FORGET.key
        };

        _this.$ajax.get(process.env.VUE_APP_SERVER + '/business/web/member/check-email-exist/' + _this.memberForget.email).then((res)=>{
          let response = res.data;
          if (response.data) {
            _this.forgetEmailValidate = true;
            _this.sendEmailboxCode(emailbox, "forget-send-code-btn");
          } else {
            _this.forgetEmailValidate = false;
            Toast.warning("邮箱未注册");
          }
        });
      },




      resetPassword() {
        let _this = this;

        // 提交之前，先校验所有输入框
        // 注意：当有一个文本框校验为false时，其它不校验
        let validateResult = _this.onForgetEmailBlur() &&
          _this.onForgetEmailCodeBlur() &&
          _this.onForgetPasswordBlur() &&
          _this.onForgetConfirmPasswordBlur();
        if (!validateResult) {
          return;
        }

        _this.memberForget.password = hex_md5(_this.memberForget.passwordOriginal + KEY);

        // 调服务端密码重置接口
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/web/member/reset-password', _this.memberForget).then((res)=>{
          let response = res.data;
          if (response.success) {
            Toast.success("密码重置成功");
            _this.toLoginDiv();
          } else {
            Toast.warning(response.message);
          }
        }).catch((response)=>{
          console.log("error：", response);
        })
      },

      //-------------------------------- 注册框校验 ----------------------------

      onRegisterEmailBlur () {
        let _this = this;
        _this.registerEmailValidate = Pattern.validateEmail(_this.memberRegister.email);
        return _this.registerEmailValidate;
      },

      onRegisterEmailCodeBlur () {
        let _this = this;
        _this.registerEmailCodeValidate = Pattern.validateEmailCode(_this.memberRegister.emailCode);
        return _this.registerEmailValidate;
      },

      onRegisterNameBlur () {
        let _this = this;
        _this.registerNameValidate = Pattern.validateName(_this.memberRegister.name);
        return _this.registerEmailValidate;
      },

      onRegisterPasswordBlur () {
        let _this = this;
        _this.registerPasswordValidate = Pattern.validatePasswordWeak(_this.memberRegister.passwordOriginal);
        return _this.registerEmailValidate;
      },

      onRegisterConfirmPasswordBlur () {
        let _this = this;
        let confirmPassword = $("#register-confirm-password").val();
        if (Tool.isEmpty(confirmPassword)) {
          return _this.registerConfirmPasswordValidate = false;
        }
        return _this.registerConfirmPasswordValidate = (confirmPassword === _this.memberRegister.passwordOriginal);
      },

      //-------------------------------- 忘记密码框校验 ----------------------------

      onForgetEmailBlur () {
        let _this = this;
        return _this.forgetEmailValidate = Pattern.validateEmail(_this.memberForget.email);
      },

      onForgetEmailCodeBlur () {
        let _this = this;
        return _this.forgetEmailCodeValidate = Pattern.validateEmailCode(_this.memberForget.emailCode);
      },

      onForgetPasswordBlur () {
        let _this = this;
        return _this.forgetPasswordValidate = Pattern.validatePasswordWeak(_this.memberForget.passwordOriginal);
      },

      onForgetConfirmPasswordBlur () {
        let _this = this;
        let forgetPassword = $("#forget-confirm-password").val();
        if (Tool.isEmpty(forgetPassword)) {
          return _this.forgetConfirmPasswordValidate = false;
        }
        return _this.forgetConfirmPasswordValidate = (forgetPassword === _this.memberForget.passwordOriginal);
      }
    }
  }
</script>

<style scoped>
  /* 登录框 */
  .login-div .input-group-addon {
    padding: 0;
    border: 0;
  }

  #login-modal h3 {
    text-align: center;
    margin-bottom: 20px;
  }

  #login-modal .modal-login {
    max-width: 400px;
  }

  #login-modal input:not(.remember) {
    height: 45px;
    font-size: 16px;
  }

  #login-modal .submit-button {
    height: 50px;
    font-size: 20px;
  }

  #login-modal .to-login-div {
    text-align: center;
  }
</style>
