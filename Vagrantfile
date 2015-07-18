Vagrant.configure(2) do |config|
  config.vm.define "upstart" do |upstart|
    upstart.vm.box = "ubuntu/trusty64"
  end

  config.vm.define "systemd" do |systemd|
    systemd.vm.box = "ubuntu/vivid64"
  end

  config.vm.define "systemv" do |systemv|
    systemv.vm.box = "puphpet/debian75-x64"
  end
end
