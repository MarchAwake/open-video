<template>
  <big-file v-bind:input-id="inputId"
            v-bind:text="text"
            v-bind:suffixs="suffixs"
            v-bind:use="use"
            v-bind:after-upload="afterUpload"
            v-bind:shard-size="512 * 1024"
            v-bind:url="'upload'">
  </big-file>
</template>

<script>
  import BigFile from "./bigFile";
  export default {
    components: {BigFile},
    name: 'vod',
    props: {
      text: {
        default: "上传视频"
      },
      inputId: {
        default: "upload-section-video"
      },
      suffixs: {
        default: []
      },
      use: {
        default: ""
      },
      afterUpload: {
        type: Function,
        default: null
      },
    },
    methods: {
    }
  }
</script>
