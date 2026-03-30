package uk.gov.di.handlers;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RelyingPartyPostHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        List<NameValuePair> pairs = URLEncodedUtils.parse(request.body(), Charset.defaultCharset());

        Map<String, String> formParameters =
                pairs.stream()
                        .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));

        response.cookie(
                "/", "relyingParty", formParameters.get("relying-party"), 3600, false, true);
        if ("on".equals(formParameters.get("use-alternative-domain"))) {
            response.cookie("/", "useAlternativeDomain", "true", 3600, false, true);
        } else {
            response.removeCookie("/", "useAlternativeDomain");
        }
        response.redirect("/");
        return null;
    }
}
