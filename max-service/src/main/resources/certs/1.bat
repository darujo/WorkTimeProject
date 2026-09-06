// Source - https://stackoverflow.com/a/68214137
// Posted by not2savvy, modified by community. See post 'Timeline' for change history
// Retrieved 2026-08-15, License - CC BY-SA 4.0
"C:\Program Files\Java\jre1.8.0_501\bin\keytool" -importcert -trustcacerts -keystore "C:\java\WorkTimeProject\gateway-service\src\main\resources\Server-keystore.p12" -file russian_trusted_root_ca.cer -alias russian_trusted_root
"C:\Program Files\Java\jre1.8.0_501\bin\keytool" -importcert -trustcacerts -keystore "C:\java\WorkTimeProject\gateway-service\src\main\resources\Server-keystore.p12" -file russian_trusted_sub_ca.cer -alias russian_trusted_sub


"C:\Program Files\Java\jre1.8.0_501\bin\keytool" -importcert -trustcacerts -keystore "C:\Program Files\Java\jre1.8.0_501\lib\security\cacerts" -file russian_trusted_root_ca.cer -alias russian_trusted_root
"C:\Program Files\Java\jre1.8.0_501\bin\keytool" -importcert -trustcacerts -keystore "C:\Program Files\Java\jre1.8.0_501\lib\security\cacerts" -file russian_trusted_sub_ca.cer -alias russian_trusted_sub

