package dev.satyrn.lunamoth.storage.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.PasswordAuthentication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MySQLConnectionSupplierTest {

    @Mock
    private Connection mockConnection;
    private MySQLConnectionSupplier supplier;

    @BeforeEach
    void setUp() {
        String hostname = "localhost";
        int port = 3306;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());
        supplier = new MySQLConnectionSupplier(hostname, port, database, credentials);
    }


    @Test
    @SuppressWarnings({"EmptyTryBlock"})
    public void testConstructorWithValidParameters() {
        // Arrange
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        // Act & Assert
        assertDoesNotThrow(() -> {
            try (var ignored = new MySQLConnectionSupplier(database, credentials)) {
                // always passes
            }
        });
    }

    @Test
    @SuppressWarnings({"ConstantConditions", "EmptyTryBlock"})
    void testConstructorWithNullDatabase() {
        // Arrange
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            try (var ignored = new MySQLConnectionSupplier(null, credentials)) {
                //won't reach here
            }
        });
    }

    @Test
    @SuppressWarnings({"ConstantConditions", "EmptyTryBlock"})
    void testConstructorWithNullCredentials() {
        // Arrange
        String database = "testDatabase";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            try (var ignored = new MySQLConnectionSupplier(database, null)) {
                //Will not reach here
            }
        });
    }

    @Test
    @SuppressWarnings({"ConstantConditions", "EmptyTryBlock"})
    void testConstructorWithNullDatabaseAndCredentials() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            try (var ignored = new MySQLConnectionSupplier(null, null)) {
               //Will not reach here
            }
        });
    }

    @Test
    @SuppressWarnings({"EmptyTryBlock"})
    void testConstructor2WithValidParameters() {
        // Arrange
        String hostname = "localhost";
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        // Act & Assert
        assertDoesNotThrow(() -> {
            try (var ignored = new MySQLConnectionSupplier(hostname, database, credentials)) {
                // always passes
            }
        });
    }

    @SuppressWarnings({"ConstantConditions", "EmptyTryBlock"})
    @Test
    void testConstructor2WithNullHostname() {
        // Arrange
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            try (var ignored = new MySQLConnectionSupplier(null, database, credentials)) {
                // will not reach here
            }
        });
    }

    @SuppressWarnings({"ConstantConditions", "EmptyTryBlock"})
    @Test
    void testConstructor2WithNullDatabase() {
        // Arrange
        String hostname = "localhost";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            try (var ignored = new MySQLConnectionSupplier(hostname, null, credentials)) {
                // will not reach here
            }
        });
    }

    @SuppressWarnings({"ConstantConditions", "EmptyTryBlock"})
    @Test
    void testConstructor2WithNullCredentials() {
        // Arrange
        String hostname = "localhost";
        String database = "testDatabase";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            try (var ignored = new MySQLConnectionSupplier(hostname, database, null)) {
                // will not reach here
            }
        });
    }

    @SuppressWarnings({"ConstantConditions", "EmptyTryBlock"})
    @Test
    void testConstructor2WithNullHostnameDatabaseAndCredentials() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            try (var ignored = new MySQLConnectionSupplier(null, null, null)) {
                // will not reach here
            }
        });
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    void testConstructor3WithValidParameters() {
        // Arrange
        String hostname = "localhost";
        int port = 3306;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        // Act & Assert
        assertDoesNotThrow(() -> {
            try (var ignored = new MySQLConnectionSupplier(hostname, port, database, credentials)) {
                // always passes
            }
        });
    }

    @Test
    @SuppressWarnings({"ConstantConditions", "EmptyTryBlock"})
    void testConstructor3WithNullHostname() {
        // Arrange
        int port = 3306;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            try (var ignored = new MySQLConnectionSupplier(null, port, database, credentials)) {
                // will not reach here
            }
        });
    }

    @Test
    @SuppressWarnings({"ConstantConditions", "EmptyTryBlock"})
    void testConstructor3WithNullDatabase() {
        // Arrange
        String hostname = "localhost";
        int port = 3306;
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            try (var ignored = new MySQLConnectionSupplier(hostname, port, null, credentials)) {
                // will not reach here
            }
        });
    }

    @Test
    @SuppressWarnings({"ConstantConditions", "EmptyTryBlock"})
    void testConstructor3WithNullCredentials() {
        // Arrange
        String hostname = "localhost";
        int port = 3306;
        String database = "testDatabase";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            try (var ignored = new MySQLConnectionSupplier(hostname, port, database, null)) {
                // will not reach here
            }
        });
    }

    @Test
    @SuppressWarnings({"ConstantConditions", "EmptyTryBlock"})
    void testConstructor3WithNullHostnameDatabaseAndCredentials() {
        // Arrange
        int port = 3306;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            try (var ignored = new MySQLConnectionSupplier(null, port, null, null)) {
                // will not reach here
            }
        });
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    void testConstructor3WithInvalidPortNegative() {
        // Arrange
        String hostname = "localhost";
        int port = -1;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            try (var ignored = new MySQLConnectionSupplier(hostname, port, database, credentials)) {
                // will not reach here
            }
        });
    }

    @Test
    @SuppressWarnings("EmptyTryBlock")
    void testConstructor3WithInvalidPortTooHigh() {
        // Arrange
        String hostname = "localhost";
        int port = 70000;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            try (var ignored = new MySQLConnectionSupplier(hostname, port, database, credentials)) {
                // will not reach here
            }
        });
    }

    @Test
    void testGetUserName() {
        // Arrange
        String hostname = "localhost";
        int port = 3306;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        // Act & Assert
        try (var instance = new MySQLConnectionSupplier(hostname, port, database, credentials)) {
            // Assert
            assertEquals("user", instance.getUserName());
        }
    }

    @Test
    void testGetUserNameWithNullUser() {
        // Arrange
        String hostname = "localhost";
        int port = 3306;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication(null, "password".toCharArray());

        // Act & Assert
        try (var instance = new MySQLConnectionSupplier(hostname, port, database, credentials)) {
            // Assert
            assertNull(instance.getUserName());
        }
    }

    @Test
    void testGetPassword() {
        // Arrange
        String hostname = "localhost";
        int port = 3306;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        // Act & Assert
        try (var instance = new MySQLConnectionSupplier(hostname, port, database, credentials)) {
            // Assert
            assertArrayEquals("password".toCharArray(), instance.getPassword());
        }
    }

    @Test
    void testGetFlags() {
        // Arrange
        String hostname = "localhost";
        int port = 3306;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        try (var instance = new MySQLConnectionSupplier(hostname, port, database, credentials)) {
            // Act
            Map<String, String> flags = instance.getFlags();

            // Assert
            assertNotNull(flags);
            assertEquals(0, flags.size()); // Initially empty
        }
    }

    @Test
    void testSetFlag() {
        // Arrange
        String hostname = "localhost";
        int port = 3306;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        try (var instance = new MySQLConnectionSupplier(hostname, port, database, credentials)) {
            // Act
            instance.setFlag("flag1", "value1");

            // Assert
            Map<String, String> flags = instance.getFlags();
            assertEquals(1, flags.size());
            assertEquals("value1", flags.get("flag1"));
        }
    }

    @Test
    void testSetFlagWithNullValue() {
        // Arrange
        String hostname = "localhost";
        int port = 3306;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        try (var instance = new MySQLConnectionSupplier(hostname, port, database, credentials)) {
            // Act
            instance.setFlag("flag1", "value1");
            instance.setFlag("flag1", null); // Should remove the flag

            // Assert
            Map<String, String> flags = instance.getFlags();
            assertEquals(0, flags.size());
        }
    }

    @Test
    void testSetFlagWithBlankValue() {
        // Arrange
        String hostname = "localhost";
        int port = 3306;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        try (var instance = new MySQLConnectionSupplier(hostname, port, database, credentials)) {
            // Act
            instance.setFlag("flag1", "value1");
            instance.setFlag("flag1", " "); // Should remove the flag

            // Assert
            Map<String, String> flags = instance.getFlags();
            assertEquals(0, flags.size());
        }
    }

    @Test
    void testSetFlagsFromMap() {
        // Arrange
        String hostname = "localhost";
        int port = 3306;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        try (var instance = new MySQLConnectionSupplier(hostname, port, database, credentials)) {
            // Act
            instance.setFlags(Map.of("flag1", "value1", "flag2", "value2"));

            // Assert
            Map<String, String> flags = instance.getFlags();
            assertEquals(2, flags.size());
            assertEquals("value1", flags.get("flag1"));
            assertEquals("value2", flags.get("flag2"));
        }
    }

    @Test
    void testSetFlagsFromList() {
        // Arrange
        String hostname = "localhost";
        int port = 3306;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        try (var instance = new MySQLConnectionSupplier(hostname, port, database, credentials)) {
            // Act
            instance.setFlags(List.of(Map.of("flag1", "value1"), Map.of("flag2", "value2")));

            // Assert
            Map<String, String> flags = instance.getFlags();
            assertEquals(2, flags.size());
            assertEquals("value1", flags.get("flag1"));
            assertEquals("value2", flags.get("flag2"));
        }
    }

    @Test
    @SuppressWarnings({"ConstantConditions"})
    void testSetFlagsWithNullMap() {
        // Arrange
        String hostname = "localhost";
        int port = 3306;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        // Act & Assert
        try (var instance = new MySQLConnectionSupplier(hostname, port, database, credentials)) {
            assertThrows(IllegalArgumentException.class, () -> {
                instance.setFlags((Map<?, ?>) null); // Should throw IllegalArgumentException
            });
        }
    }

    @Test
    @SuppressWarnings({"ConstantConditions"})
    void testSetFlagsWithNullList() {
        // Arrange
        String hostname = "localhost";
        int port = 3306;
        String database = "testDatabase";
        PasswordAuthentication credentials = new PasswordAuthentication("user", "password".toCharArray());

        // Act & Assert
        try (var instance = new MySQLConnectionSupplier(hostname, port, database, credentials)) {
            assertThrows(IllegalArgumentException.class, () -> {
                instance.setFlags((List<Map<?, ?>>) null); // Should throw IllegalArgumentException
            });
        }
    }

    @Test
    void testGetConnectionSuccess() {
        // Arrange
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            // Act
            try (var instance = supplier) {
                Connection connection = instance.get();

                // Assert
                assertNotNull(connection);
                mockedDriverManager.verify(() -> DriverManager.getConnection(anyString(), anyString(), anyString()), times(1));
            }
        }
    }

    @Test
    void testGetConnectionFailure() {
        // Arrange
        try (MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenThrow(SQLException.class);

            // Act
            try (var instance = supplier) {
                Connection connection = instance.get();

                // Assert
                assertNull(connection);
                mockedDriverManager.verify(() -> DriverManager.getConnection(anyString(), anyString(), anyString()), times(1));
            }
        }
    }

    @Test
    void testGetConnectionWhenClosed() {
        // Arrange
        supplier.close();

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, supplier::get);
        assertEquals("supplier has been disposed.", exception.getMessage());
    }
}