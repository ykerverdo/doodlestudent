#include <stdint.h>
#include <stddef.h>

int LLVMFuzzerTestOneInput(const uint8_t *data, size_t size) {
  if (size < 4) {
    return 0; // Ignore inputs smaller than 4 bytes
  }

  // Example: Check for a specific pattern in the input
  if (memcmp(data, "FUZZ", 4) == 0) {
      printf("Pattern 'FUZZ' found!\n");
  }

  // Example: Simulate processing the input
  for (size_t i = 0; i < size; i++) {
      if (data[i] == 0xFF) {
          printf("Found 0xFF at position %zu\n", i);
      }
  }
  return 0;
}
