package com.example.missguitar;

class Tools {

        // 获取一个[start, end]之间的随机数
        public static int GetRandom(int start, int end) {
                return (int)(Math.random() * (end - start + 1) + start);
        }

        // 根据音高和八度关系，获取频率
        //public static float GetHz(int pitch, int octave) {
        //}
}
