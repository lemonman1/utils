import java.io.*;
import java.util.*;

public class FileCopy {
  private static String sourceDirectory; //源文件目录
  private static String destinationDirectory; //目的文件目录
  private static Set<String> types; // 需要复制的文件后缀名类型
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("请输入源文件夹:");
    sourceDirectory = scanner.nextLine();
    System.out.println("请输入目的文件目录:");
    destinationDirectory = scanner.nextLine();
    System.out.println("请输入复制的文件类型(文件的后缀名，如 .txt 可以写多个，以空格区分开，如.txt .pdf):");
    String type = scanner.nextLine();
    String[] strings = type.split(" ");
    types = new HashSet<>(Arrays.asList(strings));
    File file = new File(sourceDirectory);
    scanner.close();
    System.out.println("复制中，请等待。。。。。。。");
    copy(file,destinationDirectory,types);
    System.out.println("复制完成！");
  }

  private static void copy(File source,String destination, Set<String> types){
    //得到该目录下的所有子文件对象数组
    File[] files = source.listFiles();
    //判断该数组是否为null或者长度为0，如果是则返回
    if (files == null || files.length == 0)return;
    //循环遍历文件数组
    for (File file : files) {
      //判断当前文件是否为空
      if (file == null)continue;
      //判断原目录是否是目标目录中的数据，防止目录重复复制
      if (file.getAbsolutePath().contains(destination))continue;
      //判断该文件是否是目录
      if (file.isDirectory()){
        //进行递归复制
         copy(file,destination+"/"+file.getName(),types);
      }else {
        //获取当前文件的名称
        String fileName = file.getName();
        //判断文件类型是否是输入的文件类型
        int index = fileName.lastIndexOf("."); //得到后缀名
        if (index < 0) return; //去除无后缀名的文件
        if (types.contains(fileName.substring(index))){ //判断该文件是否是以所需文件后缀结尾
           //构建文件夹即目标文件对象
          BufferedOutputStream bos = null;
          BufferedInputStream bis = null;
          try {
            bis = new BufferedInputStream(new FileInputStream(file));//获得文件写对象
            File destFile = new File(destination + "/" + fileName); //目标文件对象
            //判断是否存在
            if (!destFile.exists()){  //判断目的文件是否存在
              File parentFile = destFile.getParentFile();
              if (!parentFile.exists()){
                parentFile.mkdirs();//创建父文件对象
              }
              destFile.createNewFile();//创建目的文件
            }
             bos = new BufferedOutputStream(new FileOutputStream(destFile)); //的到字节缓冲输出流
            //进行文件的复制
            int bit;
            while (( bit = bis.read()) != -1){
              bos.write(bit); //进行复制
            }
          } catch (FileNotFoundException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }finally {
            try {
              //关闭流
              if (bis != null)bis.close();
              if (bos != null)bos.close();
            }catch (IOException e){
              e.printStackTrace();
            }
          }
        }
      }
    }
  }
}
