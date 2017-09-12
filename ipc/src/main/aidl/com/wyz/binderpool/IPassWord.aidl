package com.wyz.binderpool;

interface IPassWord{

    String encrypt(String content);
    String decrypt(String password);
}