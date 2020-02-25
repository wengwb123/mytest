import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class MyClass {
    public static void main(String[] args) {
        //构造一个带指定Region对象的配置类  参数：指定的区域  华北
        Configuration cfg = new Configuration(Region.region1());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传

        String accessKey = "1dCrj4u261Bu0OPOBb-go-RcsRHwcs1L2-UJNoib"; //密钥：你的AK
        String secretKey = "67Msb_flsg0RtwbyND-1TYdxYJNeHUrP2Es3ymA4"; //密钥：你的SK
        String bucket = "yingx-zwb";  //存储空间的名字

        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "E:\\aaa.jpg";  //文件本地路径
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "人民的名义.jpg";
        //根据密钥去做授权
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            //上传   文件上传
            Response response = uploadManager.put(localFilePath, key, upToken);
            //uploadManager.put(bytes, key, upToken)
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);  //文件名
            System.out.println(putRet.hash);  //件内容的hash值
            //http://q5u1l78s3.bkt.clouddn.com/照片.jpg  网络路径
            //http://q5u1l78s3.bkt.clouddn.com/人民的名义.mp4

        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }
}
