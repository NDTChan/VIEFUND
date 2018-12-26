package mobile.bts.com.viefund.CustomConnectApi;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * BT Company
 * Created by DQVu on 8/28/2016.

 */
public class CustomJSONObjectRequest extends Request<JSONObject> {
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public CustomJSONObjectRequest(int method, String url, Map<String, String> params, Response.Listener<JSONObject> reponseListener, Response.ErrorListener listener) {
        super(method, url, listener);
        this.listener = reponseListener;
        this.params = params;
    }
    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        return params;
    };
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = null;
            try {
                jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);

    }
}
