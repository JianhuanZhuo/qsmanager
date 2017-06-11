exit

jar:
javapackager -createjar -appclass cn.keepfight.qsmanager.AppLoader -srcdir target\classes -outdir archive -outfile qs.jar

deploy:
javapackager -deploy -appclass cn.keepfight.qsmanager.AppLoader -native image -srcdir archive -outdir deploy -outfile qs