FROM gitpod/workspace-full-vnc

ARG androidCommandLineToolsLinuxDownloadUrl="https://dl.google.com/android/repository/commandlinetools-linux-6858069_latest.zip"
ARG androidCommandLineToolsLinuxInstallationFile="commandlinetools-linux-6858069_latest.zip"

RUN cd $HOME \
    && wget $androidCommandLineToolsLinuxDownloadUrl \
    && unzip $androidCommandLineToolsLinuxInstallationFile \
    && mkdir -p android-sdk-linux/cmdline-tools/latest \
    && mv cmdline-tools/* android-sdk-linux/cmdline-tools/latest/ \
    && rmdir cmdline-tools/ \
    && rm $androidCommandLineToolsLinuxInstallationFile

ARG androidStudioDownloadUrl="https://redirector.gvt1.com/edgedl/android/studio/ide-zips/2020.3.1.7/android-studio-2020.3.1.7-linux.tar.gz"
ARG androidStudioInstallationFile="android-studio-2020.3.1.7-linux.tar.gz"

# RUN sudo apt-get update \
# && sudo apt-get install -y \
# libc6:i386 libncurses5:i386 libstdc++6:i386 lib32z1 libbz2-1.0:i386 \
# && sudo rm -rf /var/lib/apt/lists/*

RUN cd $HOME \
    && wget $androidStudioDownloadUrl \
    && sudo tar -xvf $androidStudioInstallationFile -C /usr/local/ \
    && rm $androidStudioInstallationFile

RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh && sdk install java 8.0.242-open /usr/local/android-studio/jre && sdk default java 8.0.242-open"

ENV JAVA_HOME="$HOME/.sdkman/candidates/java/current"

ARG androidPlatformVersion="android-30"
ARG androidBuildToolsVersion="30.0.3"
# ARG androidPlatformPreviewVersion="android-S"
# ARG androidBuildToolsPreviewVersion="31.0.0-rc1"
# ARG cmakeVersion="3.10.2.4988404"
# ARG ndkVersion="22.0.7026061"

RUN yes | android-sdk-linux/cmdline-tools/latest/bin/sdkmanager --licenses \
    && android-sdk-linux/cmdline-tools/latest/bin/sdkmanager "platforms;$androidPlatformVersion" "build-tools;$androidBuildToolsVersion" "sources;$androidPlatformVersion" 
# && android-sdk-linux/cmdline-tools/latest/bin/sdkmanager "platforms;$androidPlatformVersion" "build-tools;$androidBuildToolsVersion" "sources;$androidPlatformVersion" "platforms;$androidPlatformPreviewVersion" "build-tools;$androidBuildToolsPreviewVersion" 
# && android-sdk-linux/cmdline-tools/latest/bin/sdkmanager "platforms;$androidPlatformVersion" "build-tools;$androidBuildToolsVersion" "sources;$androidPlatformVersion" "platforms;androidPlatformPreviewVersion" "build-tools;androidBuildToolsPreviewVersion" "cmake;$cmakeVersion" "ndk;$ndkVersion"

ENV ANDROID_SDK_ROOT="$HOME/android-sdk-linux"

RUN sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-key C99B11DEB97541F0 \
    && sudo apt-add-repository https://cli.github.com/packages \
    && sudo apt update \
    && sudo apt install -y \
    gh \
    && sudo rm -rf /var/lib/apt/lists/*

RUN sudo apt-get update \
    && sudo apt-get install -y \
    zsh \
    && sudo rm -rf /var/lib/apt/lists/*

RUN cd $HOME \
    && wget https://github.com/robbyrussell/oh-my-zsh/raw/master/tools/install.sh -O - | zsh

RUN sed -i 's/_THEME=\"robbyrussell\"/_THEME=\"xiong-chiamiov-plus\"/g' ~/.zshrc

# RUN sed -i 's/plugins=(git)/plugins=(git gradle gradle-completion adb sdk common-aliases dircycle dirhistory dirpersist history copydir copyfile autojump fd git-completion git-auto-status git-prompt gitignore git-lfs git-extras last-working-dir per-directory-history perms wd safe-paste thefuck systemadmin scd pj magic-enter man command-not-found jump timer colored-man-pages jsontools colorize ripgrep httpie sprunge nmap transfer universalarchive catimg extract)/g' ~/.zshrc
