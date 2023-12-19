package com.mudxx.tool.oci.modules.services.storage.bucket.example;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.mudxx.tool.oci.modules.security.auth.example.AuthenticationProviderExample;
import com.mudxx.tool.oci.modules.services.storage.OCIStorageClientFactory;
import com.oracle.bmc.Region;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.Bucket;
import com.oracle.bmc.objectstorage.model.CreateBucketDetails;
import com.oracle.bmc.objectstorage.requests.CreateBucketRequest;
import com.oracle.bmc.objectstorage.responses.CreateBucketResponse;

public class CreateBucketExample {
    public static void main(String[] args) throws Exception {

        /* Create a service client */
        ObjectStorageClient client = OCIStorageClientFactory.getClient(AuthenticationProviderExample.getProviderModel(), Region.AP_SEOUL_1);

        /* Create a request and dependent object(s). */
        CreateBucketDetails createBucketDetails = CreateBucketDetails.builder()
                // 要求:是的 bucketName
                .name("agile-dev-bucket-20230818-1711")
                // 要求:是的 要在其中创建桶的分区的ID。
                .compartmentId("")
                // 要求:不 用于用户定义元数据的键和值
                .metadata(null)
                // 要求:不 此存储桶上启用的公共访问类型。bucket默认设置为NoPublicAccess，只允许经过身份验证的调用方访问bucket及其内容。
                // 当在桶上启用ObjectRead时，允许对GetObject、HeadObject和ListObjects操作进行公共访问。
                // 当桶上启用ObjectReadWithoutList时，允许对GetObject和HeadObject操作进行公共访问。允许的值有:NoPublicAccessObjectReadObjectReadWithoutList
                .publicAccessType(CreateBucketDetails.PublicAccessType.ObjectReadWithoutList)
                // 要求:不 桶的存储层类型。一个桶默认设置为“标准”层，这意味着该桶将被放在标准存储层中。
                // 当显式设置“Archive”层类型时，桶将被放入Archive Storage层。'storageTier'属性是不可变的年代
                .storageTier(CreateBucketDetails.StorageTier.Standard)
                // 要求:不 是否为这个桶中的对象状态改变触发事件。默认情况下，objectEventsEnabled设置为false。
                // 将objectEventsEnabled设置为true以发出对象状态更改的事件。有关事件的更多信息，请参见事件概述。
                .objectEventsEnabled(false)
                // 要求:不 资源的自由格式标记。每个标记都是一个简单的键值对，没有预定义的名称、类型或名称空间。有关更多信息，请参见资源标签。示例:{“Department”:“Finance”}
                .freeformTags(null)
                // 要求:不 为这个资源定义的标签。每个键都是预定义的，并限定在一个命名空间内。有关更多信息，请参阅资源标签
                .definedTags(null)
                // 要求:不 主加密密钥的OCID，用于调用key Management服务生成数据加密密钥或加密或解密数据加密密钥。
                .kmsKeyId("")
                // 要求:不 设置桶的版本控制状态。默认情况下，创建的桶的versioning状态为Disabled。
                // 使用此选项可以在创建桶时启用版本控制。已启用版本控制的桶中的对象将受到保护，不会被覆盖和删除。同一对象的以前版本将在桶中可用。允许的值为:启用禁用
                .versioning(CreateBucketDetails.Versioning.Disabled)
                // 要求:不 设置桶的自动分级状态。默认情况下，创建的桶为“禁用自动分级”状态。
                // 使用此选项可以在创建桶时启用自动分级。自动分级设置为InfrequentAccess的桶中的对象将根据对象的访问自动分级术语在“Standard”和“InfrequentAccess”层之间自动转换
                .autoTiering(Bucket.AutoTiering.Disabled)
                .build();

        CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                .namespaceName("")
                .createBucketDetails(createBucketDetails)
                .opcClientRequestId(IdUtil.randomUUID()).build();

        /* Send request to the Client */
        CreateBucketResponse response = client.createBucket(createBucketRequest);
        System.out.println(JSONUtil.toJsonStr(response));
    }


}

