package com.mudxx.tool.oci.modules.services.support.incident.example;

import cn.hutool.json.JSONUtil;
import com.mudxx.tool.oci.modules.security.auth.example.AuthenticationProviderExample;
import com.mudxx.tool.oci.modules.services.support.OCISupportClientFactory;
import com.oracle.bmc.Region;
import com.oracle.bmc.cims.IncidentClient;
import com.oracle.bmc.cims.model.*;
import com.oracle.bmc.cims.requests.CreateIncidentRequest;
import com.oracle.bmc.cims.responses.CreateIncidentResponse;

import java.util.ArrayList;
import java.util.Arrays;


public class CreateIncidentExample {
    public static void main(String[] args) throws Exception {

        /* Create a service client */
        IncidentClient client = OCISupportClientFactory.getClient(AuthenticationProviderExample.getProviderModel(), Region.AP_SEOUL_1);

        /* Create a request and dependent object(s). */
        CreateIncident createIncident = CreateIncident.builder()
                .compartmentId("ocid1.test.oc1..<unique_ID>EXAMPLE-compartmentId-Value")
                .ticket(CreateTicketDetails.builder()
                        .severity(CreateTicketDetails.Severity.Medium)
                        .resourceList(new ArrayList<>(Arrays.asList(CreateResourceDetails.builder()
                                .item(CreateLimitItemDetails.builder()
                                        .currentLimit(446)
                                        .currentUsage(611)
                                        .requestedLimit(642)
                                        .limitStatus(CreateLimitItemDetails.LimitStatus.Approved)
                                        .category(CreateCategoryDetails.builder()
                                                .categoryKey("EXAMPLE-categoryKey-Value").build())
                                        .subCategory(CreateSubCategoryDetails.builder()
                                                .subCategoryKey("EXAMPLE-subCategoryKey-Value").build())
                                        .issueType(CreateIssueTypeDetails.builder()
                                                .issueTypeKey("EXAMPLE-issueTypeKey-Value").build())
                                        .name("EXAMPLE-name-Value").build())
                                .region(com.oracle.bmc.cims.model.Region.Icn).build())))
                        .title("EXAMPLE-title-Value")
                        .description("EXAMPLE-description-Value")
                        .contextualData(ContextualData.builder()
                                .clientId("ocid1.test.oc1..<unique_ID>EXAMPLE-clientId-Value")
                                .schemaName("EXAMPLE-schemaName-Value")
                                .schemaVersion("EXAMPLE-schemaVersion-Value")
                                .payload("EXAMPLE-payload-Value").build()).build())
                .csi("EXAMPLE-csi-Value")
                .problemType(ProblemType.Tech)
                .contacts(new ArrayList<>(Arrays.asList(Contact.builder()
                        .contactName("EXAMPLE-contactName-Value")
                        .contactEmail("EXAMPLE-contactEmail-Value")
//                        .email("EXAMPLE-email-Value")
                        .contactPhone("EXAMPLE-contactPhone-Value")
                        .contactType(Contact.ContactType.Secondary).build())))
                .referrer("EXAMPLE-referrer-Value").build();

        CreateIncidentRequest createIncidentRequest = CreateIncidentRequest.builder()
                .createIncidentDetails(createIncident)
                .opcRequestId("DTPSIEIWHG0O2Z1HWZQR<unique_ID>")
                .ocid("EXAMPLE-ocid-Value")
                .homeregion("EXAMPLE-homeregion-Value")
//                .bearertokentype("EXAMPLE-bearertokentype-Value")
//                .bearertoken("EXAMPLE-bearertoken-Value")
//                .idtoken("EXAMPLE-idtoken-Value")
//                .domainid("EXAMPLE-domainid-Value")
                .build();

        /* Send request to the Client */
        CreateIncidentResponse response = client.createIncident(createIncidentRequest);
        System.out.println(JSONUtil.toJsonStr(response));
    }


}

