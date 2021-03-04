FROM gitpod/workspace-full

ARG androidCommandLineToolsLinuxDownloadUrl="https://dl.google.com/android/repository/commandlinetools-linux-6858069_latest.zip"
ARG androidCommandLineToolsLinuxInstallationFile="commandlinetools-linux-6858069_latest.zip"

RUN cd $HOME \
 && wget $androidCommandLineToolsLinuxDownloadUrl \
 && unzip $androidCommandLineToolsLinuxInstallationFile \
 && mkdir -p android-sdk-linux/cmdline-tools/latest \
 && mv cmdline-tools/* android-sdk-linux/cmdline-tools/latest/ \
 && rmdir cmdline-tools/ \
 && rm $androidCommandLineToolsLinuxInstallationFile

ENV JAVA_HOME="$HOME/.sdkman/candidates/java/current"

ARG androidPlatformVersion="android-30"
ARG androidBuildToolsVersion="30.0.3"

RUN yes | android-sdk-linux/cmdline-tools/latest/bin/sdkmanager --licenses \
 && android-sdk-linux/cmdline-tools/latest/bin/sdkmanager "platforms;$androidPlatformVersion" "build-tools;$androidBuildToolsVersion"

ENV ANDROID_SDK_ROOT="$HOME/android-sdk-linux"

ENV PATH="$PATH:$ANDROID_SDK_ROOT/platform-tools"

# Install zsh
RUN sudo apt-get update \
 && sudo apt-get install -y \
  zsh \
 && sudo rm -rf /var/lib/apt/lists/*

RUN wget https://github.com/robbyrussell/oh-my-zsh/raw/master/tools/install.sh -O - | zsh

# set the zsh theme 
ENV ZSH_THEME xiong-chiamiov-plus