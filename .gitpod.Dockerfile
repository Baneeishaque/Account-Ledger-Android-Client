FROM gitpod/workspace-full

RUN cd $HOME \
 && wget https://dl.google.com/android/repository/commandlinetools-linux-6858069_latest.zip \
 && unzip commandlinetools-linux-6858069_latest.zip \
 && mkdir -p android-sdk-linux/cmdline-tools/latest \
 && mv cmdline-tools/* android-sdk-linux/cmdline-tools/latest/ \
 && rmdir cmdline-tools/ \
 && rm commandlinetools-linux-6858069_latest.zip

# RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh && sdk install java 8.0.265-open && sdk default java 8.0.265-open"

# ENV JAVA_HOME="$HOME/.sdkman/candidates/java/current"

RUN yes | android-sdk-linux/cmdline-tools/latest/bin/sdkmanager --licenses \
 && android-sdk-linux/cmdline-tools/latest/bin/sdkmanager platform-tools "platforms;android-29" "build-tools;30.0.2"

ENV ANDROID_SDK_ROOT="$HOME/android-sdk-linux"
