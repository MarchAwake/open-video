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
            <th>邮箱</th>
            <th>昵称</th>
            <th>注册时间</th>
          </tr>
      </thead>

      <tbody>
          <tr v-for="member in members">
            <td>{{member.email}}</td>
            <td>{{member.name}}</td>
            <td>{{member.registerTime}}</td>
          </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
  import Pagination from "../../components/pagination";
  export default {
    components: {Pagination},
    name: "business-member",
    data: function() {
      return {
        member: {},
        members: [],
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
        _this.$ajax.post(process.env.VUE_APP_SERVER + '/business/admin/member/list', {
          page: page,
          size: _this.$refs.pagination.size,
        }).then((response)=>{
          Loading.hide();
          let resp = response.data;
          _this.members = resp.data.list;
          _this.$refs.pagination.render(page, resp.data.total);

        })
      },

    }
  }
</script>