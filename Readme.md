#TINMAN

Learning Record Store(LRS) is a system that stores the data from e-learning platform as
specified by Experience API.
Check out links below for more details:
http://tincanapi.com/learning-record-store/
http://www.adlnet.gov/tla/lrs/

For specification, check out their github page:
https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md

Elements of the statements not yet supported:
authority, attachments


##Server authentication:
LRS uses Basic Authentication as defined in RFC 2617
LMS sends a POST or PUT request to LRS with endpoint basic/request e.g. https://myexamplelrs/xapi/Basic/request
to request credentials for users.  The body of the request will be a JSON object

| Property | Type | Explanation |
| -------- | ---- | ----------- |
| scope | array of strings | based on https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#642-oauth-authorization-scope |
| expiry | ISO 8601 timestamp | expiration of this credentials, default to 4 hours |
| historical | boolean | if this is not true, LRS will reject any statement before the point it receives the credentials request |
| actors | person object | as defined by https://github.com/adlnet/xAPI-Spec/blob/master/xAPI.md#person-properties, this param is required by LRS |
| activity | array of strings | list of all activities allowed |
| registration | uuid | registration property |

##Concurrency Issues:
* Etag is  not supported

##Data Transfer
* Any DELETE requests is not supported

##Statement API
* Parameter : related_activities, related agents, format, attachment, limit, ascending, registration are not yet supported. These parameters will be ignored
