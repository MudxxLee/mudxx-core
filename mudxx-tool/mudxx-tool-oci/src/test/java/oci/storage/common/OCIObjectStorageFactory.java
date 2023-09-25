package oci.storage.common;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimplePrivateKeySupplier;
import com.oracle.bmc.events.EventsClient;
import com.oracle.bmc.objectstorage.ObjectStorageClient;

/**
 * @author laiw
 * @date 2023/8/18 11:18
 */
public class OCIObjectStorageFactory {

//    public static AuthenticationDetailsProvider getProvider() {
//        /**
//         *     userId: ocid1.user.oc1..aaaaaaaavarjdgcsw43hvdnna35vcrwjm3pqeu6jyv22z7pkuoywnspwwdsa
//         *     fingerprint: 48:17:65:e7:74:35:6f:76:7a:fb:71:ee:a0:94:0c:ba
//         *     tenancy: ocid1.tenancy.oc1..aaaaaaaambezyu2ogbzgxcgy2ni3e6ik7uzr4irlrrm7clbuargwmfsk2woa
//         *     region: ap-seoul-1
//         *     keyFile: /Users/laiwen/Documents/laiw@agilewing.net_2023-08-18T02_24_06.871Z.pem
//         */
//        String userId = "ocid1.user.oc1..aaaaaaaavarjdgcsw43hvdnna35vcrwjm3pqeu6jyv22z7pkuoywnspwwdsa";
//        String fingerprint = "48:17:65:e7:74:35:6f:76:7a:fb:71:ee:a0:94:0c:ba";
//        String tenancy = "ocid1.tenancy.oc1..aaaaaaaambezyu2ogbzgxcgy2ni3e6ik7uzr4irlrrm7clbuargwmfsk2woa";
//        Region region = Region.AP_SEOUL_1;
//        String keyFile = "/Users/laiwen/Documents/laiw@agilewing.net_2023-08-18T02_24_06.871Z.pem";
//        AuthenticationDetailsProvider provider = SimpleAuthenticationDetailsProvider.builder()
//                .userId(userId)
//                .fingerprint(fingerprint)
//                .tenantId(tenancy)
//                .region(region)
//                .privateKeySupplier(new SimplePrivateKeySupplier(keyFile))
//                .build();
//        return provider;
//    }

    public static AuthenticationDetailsProvider getProvider() {
        /**
         *     userId: ocid1.user.oc1..aaaaaaaavarjdgcsw43hvdnna35vcrwjm3pqeu6jyv22z7pkuoywnspwwdsa
         *     fingerprint: 48:17:65:e7:74:35:6f:76:7a:fb:71:ee:a0:94:0c:ba
         *     tenancy: ocid1.tenancy.oc1..aaaaaaaambezyu2ogbzgxcgy2ni3e6ik7uzr4irlrrm7clbuargwmfsk2woa
         *     region: ap-seoul-1
         *     keyFile: /Users/laiwen/Documents/laiw@agilewing.net_2023-08-18T02_24_06.871Z.pem
         */
        String userId = "ocid1.user.oc1..aaaaaaaavarjdgcsw43hvdnna35vcrwjm3pqeu6jyv22z7pkuoywnspwwdsa";
        String fingerprint = "48:17:65:e7:74:35:6f:76:7a:fb:71:ee:a0:94:0c:ba";
        String tenancy = "ocid1.tenancy.oc1..aaaaaaaambezyu2ogbzgxcgy2ni3e6ik7uzr4irlrrm7clbuargwmfsk2woa";
        Region region = Region.US_PHOENIX_1;
        String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCk0oVBhAuBFSos\n" +
                "rnJ4+QMD5/qZ3G95VhiWEZxrDTxgtsbr1fk9maK4AYuXgiROofhnBy+DcTn9tzvU\n" +
                "VhnY5TnWbJvGRJV83NTzUPNzm2CrHMbEf0nXwRN+S2yfKGFIjM2XujBMPppGAxwx\n" +
                "4vveS6mEoFGr6hfJdE9m5uabdj+Zl4lvuO4vpCJ+czNr+ViEFTWz0J1KYN+RBiLj\n" +
                "YcKIviQaIHywPvCnrBp/Ub84DLKg1C6d1KQynIZk6+0HnbZXBKYDixFThDeycN5P\n" +
                "ifjhZZIqOb+ngrKGrgKMiz7VN+4Ndc5Q82Je/FmqLVlCjTEW+4uXAnwa7QdGhkWo\n" +
                "yCEp2qWJAgMBAAECggEAAQYJq9F9HM0b2PEwZs3tFd4f8UqsKo29U1lCpmuLn3Ij\n" +
                "JB3NEXPdzdS8oDE/tnlNCJ7DzcYrPQchSP1L5cvuowVDpmSQpRjd/bDYAjTJ0eY4\n" +
                "2cmMlNlvHI6tWwOuLvwo3rNaT27oGF02lJy7QbMol1lDiJTIlwiw4Yu3jOmcFYEX\n" +
                "27iQLu9qPUanjpioQ2xc7iMmm10hjwrQwE3hRhSrwrbcdJ8Np9WkkjAddsWBwO4d\n" +
                "b+sWGPWUGMoM3/LjIv3VKVN5bR92StM8pg5YSP4HwGL0UMvL4jc0tijC/47GsdaD\n" +
                "GovOfBqIVt2KVriqF1y26zSlzEKCyhDVg4J99OPbcQKBgQDcAaa3bGS3v+GW4Uaj\n" +
                "+wt7OsLiEwEaEHFu1KZR98yQOI3I+NTQmv/djB1Fj+tvQ7KQsZyp311ZI75Ho2oa\n" +
                "/HmJVOPgEtdoMZBh5mHXd+BevgjulQ8zN1k3czqtOIPcgTg+Dww1N04wLxwBvh0g\n" +
                "zvLiR+BDYt97UvvvvBLBC9HMHQKBgQC/yaOSSDfCXmZQ56d4uvai4qxl+obRWBcI\n" +
                "5XNe+qYyxiKibiGd6yYB7l7WMKPDZ0cgGm9RufnzyPK7aCAN6BYF8Ewk7RbZYvs8\n" +
                "EbsZpvaUp4BB0ZguKYkLp8xhm5ZY3eEjZ7LzWBoR9SsAjT3cr/CVO0dUc+1v6bah\n" +
                "MAHRuYNLXQKBgQDHBZ9hkubR6v0SVpxmvzW1toSRqwt2HqXe0vPadsZROTfaNG+g\n" +
                "fnc0Xgok4Xrj3mPszR+h9HQnrVP2znewDfGMMIUsMR84HI42YgjkuIb6bgL6gVGM\n" +
                "zWch6FWOWe+/TA4q5cXvZiaPJC67zNXg1hWWadsQWqM8ludeCc8VX7HcaQKBgG0p\n" +
                "4Z/m2U4aG1TbtphimvX13bkiE1DfOiVHLRcXxjfKTXZ1B09yxCx/OJ3kC6Kyi2p7\n" +
                "gXdcSyh27aq8RtqDit2QfFbcWJAd5fC4Cu47+ch9WuSb32oPHEF1HkFEpRwqFvbn\n" +
                "FJWsRRKJoRNsUZTm1CKtHhm07qYvulMfFzn/xPrFAoGBAMvFBd4WN//62afXytXt\n" +
                "f6ge2FskkN1COUKtkZA+7F5eCceXuCga4mH3SI66h4qQNYiSlx13E9OxLs2ZchG9\n" +
                "qSveA+iBAXPuORkyrgUpcVh8qDj+BkgAn2zL6detrXuFjgCGsL+ruwTvbrMFdBRo\n" +
                "8fT+SFBvfh4UPkHD1I1Ttnxf\n" +
                "-----END PRIVATE KEY-----";
        AuthenticationDetailsProvider provider = SimpleAuthenticationDetailsProvider.builder()
                .userId(userId)
                .fingerprint(fingerprint)
                .tenantId(tenancy)
                .region(region)
                .privateKeySupplier(new StreamPrivateKeySupplier(privateKey))
                .build();
        return provider;
    }

    public static ObjectStorageClient getObjectStorageClient() {
        AuthenticationDetailsProvider provider = getProvider();
        return ObjectStorageClient.builder().build(provider);
    }

    public static EventsClient getEventsClient() {
        AuthenticationDetailsProvider provider = getProvider();
        return EventsClient.builder().build(provider);
    }

}
