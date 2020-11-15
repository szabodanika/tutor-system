require('dotenv').config()

export default {

  server: {
    port: process.env.PORT,
    host: process.env.HOST
  },

  head: {
    title: 'oktatutor',
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '' }
    ],
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
    ]
  },

  css: [
    '~/assets/css/light.scss',
  ],
  plugins: [
    { src: '~/plugins/services.js', ssr: true },
  ],
  components: true,
  buildModules: [
  ],
  modules: [
    'bootstrap-vue/nuxt',
    '@nuxtjs/axios',
    '@nuxtjs/dotenv',
    // '@nuxtjs/proxy'
  ],
  proxy: {
    // '/api': {
    //   target: 'http://localhost',
    //   pathRewrite: {
    //     '^/api' : '/'
    //   }
    // }
  // },
  // axios: {
  //   proxy: true
  },
  build: {
  }
}
