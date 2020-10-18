<template>
  <div v-bind:id="playerId">
  </div>
</template>
<script>
  export default {
    name: 'player',
    props: {
      playerId: {
        default: "player-div"
      },
    },
    data: function () {
      return {
        aliPlayer: {}, // 播放器实例
      }
    },
    methods: {
      playUrl(url) {
        let _this = this;

        //如果已经有播放器了，则将播放器div删除
        if (_this.aliPlayer) {
          _this.aliPlayer = null;
          $("#" + _this.playerId + '-player').remove();
        }

        // 初始化播放器
        $("#" + _this.playerId).append("<div class=\"prism-player\" id=\"" + _this.playerId + "-player\"></div>");
        _this.aliPlayer = new Aliplayer({
          id: _this.playerId + '-player',
          width: '100%',
          autoplay: false,
          source: url,
          cover: 'https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1689053532,4230915864&fm=26&gp=0.jpg',
        }, function (player) {
        });
      },

      playVod (vod) {
        let _this = this;
        _this.$ajax.get(process.env.VUE_APP_SERVER + '/file/web/play/' + vod).then((response)=>{
          let url = response.data.data;
          _this.playUrl(url);
        })

      }
    }
  }
</script>
