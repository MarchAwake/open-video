<template>
  <div>
      <button type="button" v-on:click="selectFile()" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-upload"></i>
        {{text}}
      </button>
      <input class="hidden" type="file" v-on:change="uploadFile()" ref="file" v-bind:id="inputId">
  </div>
</template>

<script>
  export default {
    name: 'file',
    props: {
      text: {
        default: "上传文件"
      },
      use: {
        default: ""
      },
      inputId: {
        default: "file-upload-input"
      },
      suffixs: {
        default: []
      },
      afterUpload: {
        type: Function,
        default: null
      }
    },
    data: function () {
      return {

      }
    },
    methods: {
      uploadFile() {
        let _this = this;
        let formData = new window.FormData();
        let file = _this.$refs.file.files[0];
        // 判断文件格式
        let suffixs = _this.suffixs;
        let fileName = file.name;
        let suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length).toLowerCase();
        let validateSuffix = false;
        for (let i = 0; i < suffixs.length; i++) {
          if (suffix === suffixs[i]) {
            validateSuffix = true;
            break;
          }
        }
        if (!validateSuffix) {
          Toast.warning("上传文件格式不对，只支持上传" + suffixs.join(","));
          $('#' + _this.inputId).val("");
          return;
        }
        formData.append('file', file);
        formData.append('use', _this.use);
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + "/file/admin/upload", formData).then((response) => {
          Loading.hide();
          let resp = response.data;
          console.log("上传文件成功：", resp);
          _this.afterUpload(resp);
          $('#' + _this.inputId).val("");
        })
      },

      selectFile() {
        let _this = this;
        $('#' + _this.inputId).trigger("click");
      }
    }
  }
</script>
