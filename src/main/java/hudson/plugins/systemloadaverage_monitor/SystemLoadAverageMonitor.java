package hudson.plugins.systemloadaverage_monitor;

import hudson.Extension;
import hudson.model.Computer;
import hudson.node_monitors.AbstractAsyncNodeMonitorDescriptor;
import hudson.node_monitors.ArchitectureMonitor;
import hudson.node_monitors.NodeMonitor;
import hudson.remoting.Callable;
import jenkins.security.MasterToSlaveCallable;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;


public class SystemLoadAverageMonitor extends NodeMonitor {

    @Extension
    @Symbol("systemloadaverage")
    public static final class DescriptorImpl extends AbstractAsyncNodeMonitorDescriptor<String> {
        @Override
        protected Callable<String, IOException> createCallable(Computer c) {
            return new SystemLoadAverageMonitor.MonitorTask();
        }

        @Override
        public String getDisplayName() {
            return Messages.SystemLoadAverageMonitor_DisplayName();
        }

        @Override
        public NodeMonitor newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            return new ArchitectureMonitor();
        }
    }


    private static class MonitorTask extends MasterToSlaveCallable<String, IOException> {
        private static final long serialVersionUID = 1L;


        public String call() {
            final OperatingSystemMXBean opsysMXbean = ManagementFactory.getOperatingSystemMXBean();
            return String.format("%.4f", opsysMXbean.getSystemLoadAverage());
        }
    }
}
