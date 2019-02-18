package kong.qingwei.opencv320;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.SortedTreeMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2018\1\26 0026.
 */

public interface ActivityApis {
    @POST("https://dtplus-cn-shanghai.data.aliyuncs.com/face/attribute/face/verify")
    Observable<FaceCompareResult> compare(@Body FaceCompareRequest request);


    @FormUrlEncoded
    @POST("v1/activity_order/receive_points/{order_id}")
    Observable<BaseResponse> updateActivityStatus(@Path("order_id") String orderId,
                                                  @FieldMap SortedTreeMap<String, String> map);


}
