package oci.storage;

/**
 * This is an automatically generated code sample.
 * To make this code sample work in your Oracle Cloud tenancy,
 * please replace the values for any parameters whose current values do not fit
 * your use case (such as resource IDs, strings containing ‘EXAMPLE’ or ‘unique_id’, and
 * boolean, number, and enum parameters with values not fitting your use case).
 */

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.oracle.bmc.events.EventsClient;
import com.oracle.bmc.events.model.Rule;
import com.oracle.bmc.events.requests.ListRulesRequest;
import com.oracle.bmc.events.responses.ListRulesResponse;
import oci.storage.common.OCIObjectStorageConstants;
import oci.storage.common.OCIObjectStorageFactory;


public class ListRulesExample {
    public static void main(String[] args) throws Exception {

        /* Create a service client */
        EventsClient client = OCIObjectStorageFactory.getEventsClient();

        /* Create a request and dependent object(s). */

        ListRulesRequest listRulesRequest = ListRulesRequest.builder()
                .compartmentId(OCIObjectStorageConstants.COMPARTMENT_ID)
                .limit(5)
                .page(null)
                .lifecycleState(Rule.LifecycleState.UnknownEnumValue)
                .displayName(null)
                .sortBy(ListRulesRequest.SortBy.Id)
                .sortOrder(ListRulesRequest.SortOrder.Asc)
                .opcRequestId(IdUtil.randomUUID())
                .build();

        /* Send request to the Client */
        ListRulesResponse response = client.listRules(listRulesRequest);
        System.out.println(JSONUtil.toJsonStr(response));
    }


}
