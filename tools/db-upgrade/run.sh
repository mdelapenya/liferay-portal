#DEBUG_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=15000"
LIFERAY_HOME="/home/liferay/lportal-upgrade-process"
DEVELOPMENT_LIBRARIES=${LIFERAY_HOME}/lib/development/
PORTAL_LIBRARIES=${LIFERAY_HOME}/lib/portal/
GLOBAL_LIBRARIES=${LIFERAY_HOME}/lib/global/

exportJarsInFolder() {
	for jarFile in $(ls -1 $1*.jar)
		do
		#echo "Adding $jarFile to the classpath"
		LIFERAY_CLASSPATH=$LIFERAY_CLASSPATH:$jarFile
	done
}

if [ ! $JAVA_HOME ]
then
	echo JAVA_HOME not defined.
	exit
fi

export LIFERAY_CLASSPATH="${LIFERAY_HOME}/portal-impl/classes/:${LIFERAY_HOME}/portal-service/classes/:${LIFERAY_HOME}/util-java/classes:${LIFERAY_HOME}/util-taglib/classes/:lib/"

exportJarsInFolder $DEVELOPMENT_LIBRARIES
exportJarsInFolder $PORTAL_LIBRARIES
exportJarsInFolder $GLOBAL_LIBRARIES
exportJarsInFolder "lib/"

$JAVA_HOME/bin/java $DEBUG_OPTS -Xmx1024m -XX:MaxPermSize=512M -Dfile.encoding=UTF8 -Duser.country=US -Duser.language=en -Duser.timezone=GMT -cp $LIFERAY_CLASSPATH com.liferay.portal.tools.DBUpgrader
