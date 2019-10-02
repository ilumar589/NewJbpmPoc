const path = require('path');
const webpack = require('webpack');
const { BaseHrefWebpackPlugin } = require('base-href-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const ForkTsCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');
const utils = require('./utils.js');

const getTsLoaderRule = env => {
  const rules = [
    {
      loader: 'cache-loader',
      options: {
        cacheDirectory: path.resolve('target/cache-loader')
      }
    },
    {
      loader: 'thread-loader',
      options: {
        // There should be 1 cpu for the fork-ts-checker-webpack-plugin.
        // The value may need to be adjusted (e.g. to 1) in some CI environments,
        // as cpus() may report more cores than what are available to the build.
        workers: require('os').cpus().length - 1
      }
    },
    {
      loader: 'ts-loader',
      options: {
        transpileOnly: true,
        happyPackMode: true
      }
    }
  ];
  if (env === 'development') {
    rules.unshift({
      loader: 'react-hot-loader/webpack'
    });
  }
  return rules;
};

module.exports = options => ({
  cache: options.env !== 'production',
  resolve: {
    extensions: [
      '.js', '.jsx', '.ts', '.tsx', '.json'
    ],
    modules: ['node_modules'],
    alias: {
      app: utils.root('src/main/webapp/app/')
    }
  },
  module: {
    rules: [ {
      test: /\.jsx?$/,
      loaders: ['babel-loader', 'babel-loader?compact=false'],
      exclude: /node_modules/
    }, {
      test: /(\.module\.scss)/,
      use: [
        {
          loader: MiniCssExtractPlugin.loader,
        },
        'css-loader'
      ]
    }, {
      test: /^((?!\.module).)*\.scss$/,
      use: [
        {
          loader: MiniCssExtractPlugin.loader
        },
        'css-loader'
      ]
    }, {
      // Transform our own .css files with PostCSS and CSS-modules
      test: /\.css$/,
      exclude: /node_modules/,
      use: ['style-loader', 'css-loader']
    }, {
      // Do not transform vendor's CSS with CSS-modules The point is that they remain
      // in global scope. Since we require these CSS files in our JS or CSS files,
      // they will be a part of our compilation either way. So, no need for
      // MiniCssExtractPlugin here.
      test: /\.css$/,
      include: /node_modules/,
      use: ['style-loader', 'css-loader']
    }, {
      test: /\.(png|jpg|gif)$/,
      use: [{
        loader: 'url-loader',
        options: {
          limit: 8192,
          mimetype: 'image/png',
          name: '[name].[ext]',
          outputPath: './images/',
          publicPath: '../images/'
        }
      }]
    }, {
      test: /\.eot(\?v=\d+.\d+.\d+)?$/,
      use: [{
        loader: 'file-loader',
        options: {
          name: '[name].[ext]',
          outputPath: './fonts/',
          publicPath: '../fonts/'
        }
      }]
    }, {
      test: /\.woff(2)?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
      use: [{
        loader: 'url-loader',
        options: {
          limit: 8192,
          mimetype: 'application/font-woff',
          name: '[name].[ext]',
          outputPath: './fonts/',
          publicPath: '../fonts/'
        }
      }]
    }, {
      test: /\.[ot]tf(\?v=\d+.\d+.\d+)?$/,
      use: [{
        loader: 'url-loader',
        options: {
          limit: 8192,
          mimetype: 'application/octet-stream',
          name: '[name].[ext]',
          outputPath: './fonts/',
          publicPath: '../fonts/'
        }
      }]
    }, {
      test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
      use: [{
        loader: 'url-loader',
        options: {
          limit: 8192,
          mimetype: 'image/svg+xml',
          name: '[name].[ext]',
          outputPath: './images/',
          publicPath: '../images/'
        }
      }]
    }, {
      test: /\.json$/,
      loader: 'json-loader'
    }]
  },
  stats: {
    children: false
  },
  optimization: {
    splitChunks: {
      cacheGroups: {
        commons: {
          test: /[\\/]node_modules[\\/]/,
          name: 'vendors',
          chunks: 'all'
        }
      }
    }
  },
  plugins: [
    new webpack.DefinePlugin({
      'process.env': {
        NODE_ENV: `'${options.env}'`,
        // APP_VERSION is passed as an environment variable from the Gradle / Maven build tasks.
        VERSION: `'${process.env.hasOwnProperty('APP_VERSION') ? process.env.APP_VERSION : 'UNKNOWN'}'`,
        DEBUG_INFO_ENABLED: options.env === 'development',
        // The root URL for API calls, ending with a '/' - for example: `"https://www.jhipster.tech:8081/myservice/"`.
        // If this URL is left empty (""), then it will be relative to the current context.
        // If you use an API server, in `prod` mode, you will need to enable CORS
        // (see the `jhipster.cors` common JHipster property in the `application-*.yml` configurations)
        SERVER_API_URL: `''`
      }
    }),

    new MiniCssExtractPlugin({
      filename: './styles/style.css',
      disable: false,
      allChunks: true
    }),
    new CopyWebpackPlugin([
      { from: './node_modules/swagger-ui/dist/css', to: 'swagger-ui/dist/css' },
      { from: './node_modules/swagger-ui/dist/lib', to: 'swagger-ui/dist/lib' },
      { from: './node_modules/swagger-ui/dist/swagger-ui.min.js', to: 'swagger-ui/dist/swagger-ui.min.js' },
      { from: './src/main/webapp//swagger-ui/', to: 'swagger-ui' },
      { from: './src/main/webapp/content/', to: 'content' },
      { from: './src/main/webapp/favicon.ico', to: 'favicon.ico' },
      { from: './src/main/webapp/manifest.webapp', to: 'manifest.webapp' },
      // jhipster-needle-add-assets-to-webpack - JHipster will add/remove third-party resources in this array
      { from: './src/main/webapp/robots.txt', to: 'robots.txt' }
    ]),
    new HtmlWebpackPlugin({
      template: './src/main/webapp/index.html',
      chunksSortMode: 'dependency',
      inject: 'body'
    }),
    new BaseHrefWebpackPlugin({ baseHref: '/' }),
  ]
});
