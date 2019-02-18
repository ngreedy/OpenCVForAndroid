package kong.qingwei.opencv320;

public class FaceCompareResult {
    public int errno;
    public String err_msg;
    public String request_id;
    public float confidence;

    @Override
    public String toString() {
        return "FaceCompareResult{" +
                "errno=" + errno +
                ", err_msg='" + err_msg + '\'' +
                ", request_id='" + request_id + '\'' +
                ", confidence=" + confidence +
                '}';
    }
}
