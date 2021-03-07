# TODO : check for existing android sdk folder
cd $HOME && wget "https://dl.google.com/android/repository/commandlinetools-linux-6858069_latest.zip" && unzip "commandlinetools-linux-6858069_latest.zip" && mkdir -p android-sdk-linux/cmdline-tools && mv cmdline-tools latest && mv latest android-sdk-linux/cmdline-tools/ && rm commandlinetools-linux-6858069_latest.zip
# TODO : check for existing of required packages
yes | android-sdk-linux/cmdline-tools/latest/bin/sdkmanager --licenses && android-sdk-linux/cmdline-tools/latest/bin/sdkmanager "platforms;android-30" "build-tools;30.0.3"
export ANDROID_SDK_ROOT="$HOME/android-sdk-linux"
export PATH="$PATH:$ANDROID_SDK_ROOT/platform-tools"
curl -s https://packagecloud.io/install/repositories/github/git-lfs/script.deb.sh | sudo bash
sudo apt-get update  && sudo apt-get install -y   zsh   python-pygments   fd-find   fzf ripgrep silversearcher-ag   git-extras   git-flow   git-lfs   httpie   autojump   nmap   imagemagick  && sudo rm -rf /var/lib/apt/lists/*
cd $HOME && rm -rf .oh-my-zsh/ && wget https://github.com/robbyrussell/oh-my-zsh/raw/master/tools/install.sh -O - | zsh
sed -i 's/_THEME=\"robbyrussell\"/_THEME=\"xiong-chiamiov-plus\"/g' ~/.zshrc
git clone "https://github.com/datasift/gitflow.git"  && cd gitflow  && sudo ./install.sh  && cd ..  && sudo rm -rf gitflow
sudo pip3 install thefuck
cd $HOME  && git clone "https://github.com/davidde/git.git" ".oh-my-zsh/custom/plugins/git"
cd $HOME  && wget "https://gist.githubusercontent.com/oshybystyi/475ee7768efc03727f21/raw/4bfd57ef277f5166f3070f11800548b95a501a19/git-auto-status.plugin.zsh" -P ".oh-my-zsh/custom/plugins/git-auto-status/"
wget "http://kassiopeia.juls.savba.sk/~garabik/software/grc/grc_1.12-1_all.deb"  && sudo dpkg -i grc_1.12-1_all.deb  && rm grc_1.12-1_all.deb
cd $HOME  && git clone "https://github.com/gradle/gradle-completion" ".oh-my-zsh/custom/plugins/gradle-completion/"
cd $HOME  && git clone "https://github.com/bobthecow/git-flow-completion" ".oh-my-zsh/custom/plugins/git-flow-completion/"
cd $HOME  && wget -P ".oh-my-zsh/custom/plugins/git-completion/" "https://raw.githubusercontent.com/git/git/master/contrib/completion/git-completion.bash"  && wget -P ".oh-my-zsh/custom/plugins/git-completion/" "https://raw.githubusercontent.com/git/git/master/contrib/completion/git-completion.tcsh"  && wget -O ".oh-my-zsh/custom/plugins/git-completion/git-completion.plugin.zsh" "https://raw.githubusercontent.com/git/git/master/contrib/completion/git-completion.zsh"  && wget -P ".oh-my-zsh/custom/plugins/git-completion/" "https://raw.githubusercontent.com/git/git/master/contrib/completion/git-prompt.sh"
sed -i 's/plugins=(git)/plugins=(git gradle gradle-completion adb sdk common-aliases dircycle dirhistory dirpersist history copydir copyfile autojump fd git-completion git-auto-status git-prompt gitfast gitignore git-flow git-flow-completion git-flow-avh git-hubflow git-lfs git-extras last-working-dir per-directory-history perms wd safe-paste thefuck systemadmin scd pj magic-enter man command-not-found jump timer colored-man-pages jsontools grc colorize ripgrep httpie sprunge nmap transfer universalarchivecatimg extract)/g' ~/.zshrc
echo "cd Account-Ledger-Android-Client" > ~/.zshenv
zsh