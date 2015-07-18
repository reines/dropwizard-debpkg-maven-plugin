Vagrant.configure(2) do |config|
  config.vm.define "upstart" do |upstart|
    upstart.vm.box = "ubuntu/trusty64"
    upstart.vm.provision "shell", inline: "apt-get update && apt-get install -qy openjdk-7-jdk"
  end

  config.vm.define "systemd" do |systemd|
    systemd.vm.box = "ubuntu/vivid64"
    systemd.vm.provision "shell", inline: "apt-get update && apt-get install -qy openjdk-7-jdk"
  end

  config.vm.define "systemv" do |systemv|
    systemv.vm.box = "puphpet/debian75-x64"
    systemv.vm.provision "shell", inline: "apt-get update && apt-get install -qy openjdk-7-jdk"
  end
end
