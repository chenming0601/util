package com.chenming.javaUtil.audioUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created with Intellij IDEA
 * User : chenming
 * Date : 2018-08-28
 * Time : 10:04
 * Description : 将pcm格式音频文件转为wav格式音频文件
 **/

public class Pcm2Wav {

    /**
      * Description : **
      * param : [source, target] [pcm文件路径K“D:\123.pcm”，wav目标文件保存路径“D:\123.wav”]
      * return : void
      **/
    public static void convertAudioFiles(String source, String target) throws Exception {
        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = new FileOutputStream(target);

        //计算长度
        byte[] buf = new byte[1024 * 4];
        int size = fis.read(buf);
        int PCMSize = 0;
        while (size != -1) {
            PCMSize += size;
            size = fis.read(buf);
        }
        fis.close();


        //填入参数，比特率等等。这里用的是16位单声道 8000 hz
        WaveHeader header = new WaveHeader();
        //长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
        header.fileLength = PCMSize + (44 - 8);
        header.FmtHdrLeth = 16;
        header.BitsPerSample = 16;//每个样本中的位数 采样精度16位
        header.Channels = 1;//声道数(单声道 1 个,立体声 2 个)
        header.FormatTag = 0x0001;
        header.SamplesPerSec = 16000;//每秒的样本数 采样率16
        header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
        header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
        header.DataHdrLeth = PCMSize;

        byte[] h = header.getHeader();

        assert h.length == 44; //WAV标准，头部应该是44字节
        //write header
        fos.write(h, 0, h.length);
        //write data stream
        fis = new FileInputStream(source);
        size = fis.read(buf);
        while (size != -1) {
            fos.write(buf, 0, size);
            size = fis.read(buf);
        }
        fis.close();
        fos.close();
        System.out.println("Convert OK!");
    }

}
