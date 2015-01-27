package com.github.blacklocus.rdsecho;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoCfg {

    private static final Logger LOG = LoggerFactory.getLogger(EchoCfg.class);

    public static final String PREFIX = "rdsecho.";

    // Required, must be defined
    public static final String PROP_INTERACTIVE = PREFIX + "interactive";
    public static final String PROP_NAME = PREFIX + "name";
    public static final String PROP_REGION = PREFIX + "region";
    public static final String PROP_ACCOUNT_NUMBER = PREFIX + "accountNumber";
    public static final String PROP_SNAPSHOT_DB_INSTANCE_IDENTIFIER = PREFIX + "snapshot.dbInstanceIdentifier";

    // All new instance parameters are required
    public static final String PROP_NEW_ENGINE = PREFIX + "new.engine";
    public static final String PROP_NEW_LICENSE_MODEL = PREFIX + "new.licenseModel";
    public static final String PROP_NEW_DB_INSTANCE_CLASS = PREFIX + "new.dbInstanceClass";
    public static final String PROP_NEW_MULTI_AZ = PREFIX + "new.multiAz";
    public static final String PROP_NEW_STORAGE_TYPE = PREFIX + "new.storageType";
    public static final String PROP_NEW_IOPS = PREFIX + "new.iops";
    public static final String PROP_NEW_PORT = PREFIX + "new.port";
    public static final String PROP_NEW_OPTION_GROUP_NAME = PREFIX + "new.optionGroupName";
    public static final String PROP_NEW_AUTO_MINOR_VERSION_UPGRADE = PREFIX + "new.autoMinorVersionUpgrade";

    // Modify parameters are mostly optional
    public static final String PROP_MOD_DB_PARAMETER_GROUP_NAME = PREFIX + "mod.dbParameterGroupName";
    public static final String PROP_MOD_DB_SECURITY_GROUPS = PREFIX + "mod.dbSecurityGroups";
    public static final String PROP_MOD_BACKUP_RETENTION_PERIOD = PREFIX + "mod.backupRetentionPeriod";
    public static final String PROP_MOD_APPLY_IMMEDIATELY = PREFIX + "mod.applyImmediately";

    // Promote parameters are required
    public static final String PROP_PROMOTE_CNAME = PREFIX + "promote.cname";
    public static final String PROP_PROMOTE_TTL = PREFIX + "promote.ttl";

    // Retire parameters are optional and unspecified take on AWS defaults
    public static final String PROP_RETIRE_SKIP_FINAL_SNAPSHOT = PREFIX + "retire.skipFinalSnapshot";
    public static final String PROP_RETIRE_FINAL_DB_SNAPSHOT_IDENTIFIER = PREFIX + "retire.finalDbSnapshotIdentifier";

    final String[] required = new String[]{
            PROP_INTERACTIVE,
            PROP_NAME,
            PROP_REGION,
            PROP_ACCOUNT_NUMBER,
            PROP_SNAPSHOT_DB_INSTANCE_IDENTIFIER,
            PROP_NEW_ENGINE,
            PROP_NEW_LICENSE_MODEL,
            PROP_NEW_DB_INSTANCE_CLASS,
            PROP_NEW_MULTI_AZ,
            PROP_NEW_STORAGE_TYPE,
            PROP_NEW_IOPS,
            PROP_NEW_PORT,
            PROP_NEW_OPTION_GROUP_NAME,
            PROP_NEW_AUTO_MINOR_VERSION_UPGRADE,
            PROP_MOD_APPLY_IMMEDIATELY,
            PROP_PROMOTE_CNAME,
            PROP_PROMOTE_TTL,
    };
    final CompositeConfiguration cfg;

    public EchoCfg() {
        this.cfg = new CompositeConfiguration();
        this.cfg.addConfiguration(new SystemConfiguration());
        try {
            this.cfg.addConfiguration(new PropertiesConfiguration(EchoConst.CONFIGURATION_PROPERTIES));
            LOG.info("Reading configuration from {}", EchoConst.CONFIGURATION_PROPERTIES);

        } catch (ConfigurationException e) {
            LOG.info("{} will not be read because {}", EchoConst.CONFIGURATION_PROPERTIES, e.getMessage());
        }
        validate();
    }

    void validate() {
        for (String prop : required) {
            Preconditions.checkState(cfg.containsKey(prop), prop + " must be defined");
        }
    }

    public boolean interactive() {
        return cfg.getBoolean(PROP_INTERACTIVE);
    }

    public String name() {
        return cfg.getString(PROP_NAME);
    }

    public String region() {
        return cfg.getString(PROP_REGION);
    }

    public String accountNumber() {
        return cfg.getString(PROP_ACCOUNT_NUMBER);
    }

    public String snapshotDbInstanceIdentifier() {
        return cfg.getString(PROP_SNAPSHOT_DB_INSTANCE_IDENTIFIER);
    }

    public String newEngine() {
        return cfg.getString(PROP_NEW_ENGINE);
    }

    public String newLicenseModel() {
        return cfg.getString(PROP_NEW_LICENSE_MODEL);
    }

    public String newDbInstanceClass() {
        return cfg.getString(PROP_NEW_DB_INSTANCE_CLASS);
    }

    public boolean newMultiAz() {
        return cfg.getBoolean(PROP_NEW_MULTI_AZ);
    }

    public String newStorageType() {
        return cfg.getString(PROP_NEW_STORAGE_TYPE);
    }

    public int newIops() {
        return cfg.getInt(PROP_NEW_IOPS);
    }

    public int newPort() {
        return cfg.getInt(PROP_NEW_PORT);
    }

    public String newOptionGroupName() {
        return cfg.getString(PROP_NEW_OPTION_GROUP_NAME);
    }

    public boolean newAutoMinorVersionUpgrade() {
        return cfg.getBoolean(PROP_NEW_AUTO_MINOR_VERSION_UPGRADE);
    }


    public Optional<String> modDbParameterGroupName() {
        return Optional.fromNullable(cfg.getString(PROP_MOD_DB_PARAMETER_GROUP_NAME));
    }

    public Optional<String[]> modDbSecurityGroups() {
        String[] values = cfg.getStringArray(PROP_MOD_DB_SECURITY_GROUPS);
        if (values == null || values.length == 0) {
            return Optional.absent();
        } else {
            return Optional.of(values);
        }
    }

    public Optional<Integer> modBackupRetentionPeriod() {
        return Optional.fromNullable(cfg.getInteger(PROP_MOD_BACKUP_RETENTION_PERIOD, null));
    }

    public boolean modApplyImmediately() {
        return cfg.getBoolean(PROP_MOD_APPLY_IMMEDIATELY);
    }

    public String promoteCname() {
        return cfg.getString(PROP_PROMOTE_CNAME);
    }

    public long promoteTtl() {
        return cfg.getLong(PROP_PROMOTE_TTL);
    }

    public Optional<Boolean> retireSkipFinalSnapshot() {
        return Optional.fromNullable(cfg.getBoolean(PROP_RETIRE_SKIP_FINAL_SNAPSHOT, null));
    }

    public Optional<String> retireFinalDbSnapshotIdentifier() {
        return Optional.fromNullable(cfg.getString(PROP_RETIRE_FINAL_DB_SNAPSHOT_IDENTIFIER));
    }
}