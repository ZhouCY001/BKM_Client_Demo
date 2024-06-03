package utils;

public class PrinterCommand 
{
    /**
     * 打印行缓冲器里的内容并向前走纸一行。当行缓冲器为空时只向前走纸一行。
     * 
     * @return
     */
    static public byte[] linefeed() 
    {
    	return new byte[] { (byte) 0x0A };
    }

    /**
     * 打印位置跳到下一个制表位,制表位为 8 个字符的起始位置
     * 
     * @return
     */
    static public byte[] toNextTabPosition()
    {
    	return new byte[] { (byte) 0x09 };
    }

    /**
     * 打印行缓冲区里的内容,并向前走纸 n 行。 行高为 ESC 2,ESC 3 设定的值
     * 
     * @param n
     *            0-255
     * @return
     */
    static public byte[] feedLine(int n)
    {
    	return new byte[] { (byte) 0x1B, (byte) 0x64, (byte) n };
    }

    /**
     * 1:打印机处于连线模式,接受打印数据并打印 0:打印机处于离线模式,不接受打印数据
     * 
     * @param n
     *            :0,1最低位有效
     * @return
     */
    static public byte[] setPrinterMode(int n)
    {
    	return new byte[] { (byte) 0x1B, (byte) 0x3d, (byte) n };
    }

    /*--------------------------行间距设置命令-----------------------------*/

    /**
     * 设置行间距为 4 毫米,32 点
     * 
     * @return
     */
    static public byte[] setDefaultLineHeight() 
    {
    	return new byte[] { (byte) 0x1B, (byte) 0x32 };
    }

    /**
     * 设置行间距为 n 点行。 默认值行间距是 32 点。
     * 
     * @param n
     *            :0-255
     * @return
     */
    static public byte[] setLineHeight(int n) 
    {
    	return new byte[] { (byte) 0x1B, (byte) 0x33, (byte) n };
    }

    /**
     * 设置打印行的对齐方式,缺省:左对齐 0 ≤ n ≤ 2 或 48 ≤ n ≤ 50 左对齐: n=0,48 
     *               居中对齐: n=1,49 
     *               右对齐: n=2,50
     * 
     * @param n
     *            :0 ≤ n ≤ 2 或 48 ≤ n ≤ 50
     * @return
     */
    static public byte[] setAlignMode(int n)
    {
    	return new byte[] { (byte) 0x1B, (byte) 0x61, (byte) n };
    }

    /*--------------------------字符设置命令-----------------------------*/

    /**
     * 用于设置打印字符的方式。默认值是 0
     * 
     * @param n
     *            位 0:保留 位 1:1:字体反白 位 2:1:字体上下倒置 位 3:1:字体加粗 位 4:1:双倍高度 位
     *            5:1:双倍宽度 位 6:1:删除线
     * @return
     */
    static public byte[] setFont(int n) 
    {
    	return new byte[] { (byte) 0x1B, (byte) 0x21, (byte) n };
    }

    /**
     * 用于设置打印字符中文小字体的方式。默认值是 0
     * 
     * @param n
     *            
     * @return
     */
    static public byte[] setFontCN(int n) 
    {
    	return new byte[] { (byte) 0x1C, (byte) 0x21, (byte) n };
    }
    
    /**
     * n 的低 4 位表示高度是否放大,等于 0 表示不放大 n 的高 4 位表示宽度是否放大,等于 0 表示不放大
     * 
     * @param n
     * @return
     */
    static public byte[] setFontEnlarge(int n)
    {
    	return new byte[] { (byte) 0x1D, (byte) 0x21, (byte) n };
    }

    /**
     * 等于 0 时取消字体加粗 非 0 时设置字体加粗
     * 
     * @param n
     *            最低位有效
     * @return
     */
    static public byte[] setFontBold(int n)
    {
    	return new byte[] { (byte) 0x1B, (byte) 0x45, (byte) n };
    }

    /**
     * 默认值:0
     * 
     * @param n
     *            :表示两个字符之间的间距
     * @return
     */
    static public byte[] setFontDistance(int n)
    {
    	return new byte[] { (byte) 0x1B, (byte) 0x20, (byte) n };
    }

    /**
     * 默认:0
     * 
     * @param n
     *            n=1:设置字符上下倒置 n=0:取消字符上下倒置
     * @return
     */
    static public byte[] setCharacterInvert(int n) 
    {
    	return new byte[] { (byte) 0x1B, (byte) 0x7B, (byte) n };
    }

    /**
     * 默认:0
     * 
     * @param n
     *            n=0-2,下划线的高度
     * @return
     */
    static public byte[] setUnderlineHeight(int n)
    {
    	return new byte[] { (byte) 0x1B, (byte) 0x2D, (byte) n };
    }

    /**
     * 
     * @param n
     *            n=1:选择用户自定义字符集; n=0:选择内部字符集(默认)
     * @return
     */
    static public byte[] setCustomCharacterSet(int n) 
    {
    	return new byte[] { (byte) 0x1B, (byte) 0x25, (byte) n };
    }

    /**
     * 选择国际字符集。中文版本不支持该命令。
     * 
     * @param n
     *            国际字符集设置如下:0:USA 1:France 2:Germany 3:U.K. 4:Denmark 1 5:Sweden
     *            6:Italy 7:Spain1 8:Japan 9:Norway 10:Denmark II 11:Spain II
     *            12:Latin America 13:Korea
     * @return
     */
    static public byte[] setInternationalCharacterSet(int n)
    {
    	return new byte[] { (byte) 0x1B, (byte) 0x52, (byte) n };
    }

    /**
     * 选择字符代码页,字符代码页用于选择 0x80~0xfe 的打印字符。中文版本不支持该命令
     * 
     * @param n
     *            字符代码页参数如 下:0:437 1:850
     * @return
     */
    static public byte[] setCharacterCodePage(int n)
    {
    	return new byte[] { (byte) 0x1B, (byte) 0x74, (byte) n };
    }

    /*--------------------------初始化命令-----------------------------*/

    /**
     * 初始化打印机。清除打印缓冲区 恢复默认值 选择字符打印方式 删除用户自定义字符
     * 
     * @return
     */
    static public byte[] init()
    {
    	return new byte[] { (byte) 0x1B, (byte) 0x40 };
    }

    /*--------------------------状态传输命令-----------------------------*/

    static public byte[] setHeatTime(int n)
    {
    	return new byte[]{ 0x1B, 0x37, 7,(byte)n, 2};
    }
    
	static public byte[] cutPaper()
	{
		return new byte[] { (byte)0x1D,0x56,0x42, 0x00 };
	}
   /*--------------------------条码打印命令 略 -----------------------------*/

    /*--------------------------控制板参数命令 略 -----------------------------*/

    /**
     * 自定义制表位(2个空格)
     * 
     * @return
     */
    static public byte[] getCustomTabs()
    {
    	return "  ".getBytes();
    }


    //设置打印浓度
    static public byte[] setConcentration(int n){return new byte[]{0x1D,0x45,(byte)n};}

    //设置反打
    static public byte[] setReversePrint(int n){return new byte[]{0x1D,0x42,(byte)n};}

}
