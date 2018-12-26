package mobile.bts.com.viefund.CustomConnectApi;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;

import java.util.Map;

/**
 * Created by DQV on 12/13/2016.
 */

public class InputStreamVolleyRequest extends Request<byte[]> {
    private final Response.Listener<byte[]> mListener;
    private Map<String,String> mParams;
    public Map<String, String> responseHeaders ;

    public InputStreamVolleyRequest(int post, String mUrl, Map<String, String> mParams, Response.Listener<byte[]> listener,
                                    Response.ErrorListener errorListener) {
        // TODO Auto-generated constructor stub

        super(post, mUrl, errorListener);
        // this request would never use cache since you are fetching the file content from server
        setShouldCache(false);
        mListener = listener;
        this.mParams=mParams;
    }
    @Override
    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return mParams;
    };


    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

        //Initialise local responseHeaders map with response headers received
        responseHeaders = response.headers;

        //Pass the response data here
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }}
