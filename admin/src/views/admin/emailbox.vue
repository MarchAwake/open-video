<template>
  <div>
    <p>
      <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh"></i>
        刷新
      </button>
    </p>

    <pagination ref="pagination" v-bind:list="list" v-bind:itemCount="8"></pagination>

    <table id="simple-table" class="table  table-bordered table-hover">
      <thead>
      <tr>
        <th>会员邮箱</th>
        <th>验证码</th>
        <th>用途</th>
        <th>状态</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="emailbox in emailboxs">
        <td>{{emailbox.email}}</td>
        <td>{{emailbox.code}}</td>
        <td>{{EMAILBOX_USE | optionKV(emailbox.use)}}</td>
        <td>{{EMAILBOX_STATUS | optionKV(emailbox.status)}}</td>
      </tr>
      </tbody>
    </table>

  </div>
</template>

<script>
  import Pagination from "../../components/pagination";
  export default {
    components: {Pagination},
    name: "business-emailbox",
    data: function() {
      return {
        emailbox: {},
        emailboxs: [],
        EMAILBOX_USE: EMAILBOX_USE,
        EMAILBOX_STATUS: EMAILBOX_STATUS,
      }
    },
    mounted: function() {

      let _this = this;
      _this.$refs.pagination.size = 5;
      _this.list(1);
    },

    methods: {

      /**
       * 列表查询
       */
      list(page) {
        let _this = this;
        Loading.show();
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/emailbox/list', {
          page: page,
          size: _this.$refs.pagination.size,
        }).then((response)=>{
          Loading.hide();
          let resp = response.data;
          _this.emailboxs = resp.data.list;
          _this.$refs.pagination.render(page, resp.data.total);

        })
      }
    }

  }
</script>