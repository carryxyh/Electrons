/**
 * Created by xiuyuhang on 2017/3/9.
 *
 * 监听器链相关。
 * 实际上不是真正的链，而是模仿链。构建出来的chain含有before属性，在handle的时候需要通过disruptor的handlebefore
 *
 */
package com.ziyuan.chain;