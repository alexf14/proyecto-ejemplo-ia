#!/bin/bash
# =============================================================================
# Hook: Verificador de convenciones para Spring Boot + Java 21
# Se ejecuta al final de cada sesión de Copilot para validar que el código
# generado cumple con las convenciones del proyecto.
# =============================================================================

set -euo pipefail

CHECK_MODE="${CHECK_MODE:-warn}"
ISSUES_FOUND=0

echo "🔍 Verificando convenciones del proyecto..."
echo "============================================="

# 1. Verificar que no hay @Autowired en campos
echo ""
echo "📋 Comprobando inyección de dependencias..."
AUTOWIRED_FIELDS=$(grep -rn "@Autowired" --include="*.java" src/ 2>/dev/null | grep -v "constructor" || true)
if [ -n "$AUTOWIRED_FIELDS" ]; then
    echo "⚠️  Se encontró @Autowired en campos (usar inyección por constructor):"
    echo "$AUTOWIRED_FIELDS"
    ISSUES_FOUND=$((ISSUES_FOUND + 1))
else
    echo "✅ Inyección por constructor correcta"
fi

# 2. Verificar que no hay System.out.println
echo ""
echo "📋 Comprobando uso de logging..."
SYSOUT=$(grep -rn "System\.out\.println\|System\.err\.println" --include="*.java" src/main/ 2>/dev/null || true)
if [ -n "$SYSOUT" ]; then
    echo "⚠️  Se encontró System.out.println (usar SLF4J Logger):"
    echo "$SYSOUT"
    ISSUES_FOUND=$((ISSUES_FOUND + 1))
else
    echo "✅ Logging con SLF4J correcto"
fi

# 3. Verificar que los DTOs son Records
echo ""
echo "📋 Comprobando DTOs como Records..."
DTO_CLASSES=$(find src/main/java -path "*/dto/*.java" -exec grep -l "^public class" {} \; 2>/dev/null || true)
if [ -n "$DTO_CLASSES" ]; then
    echo "⚠️  DTOs que no son Records (considerar migrar):"
    echo "$DTO_CLASSES"
    ISSUES_FOUND=$((ISSUES_FOUND + 1))
else
    echo "✅ Todos los DTOs son Records"
fi

# 4. Verificar que los Controllers usan ResponseEntity
echo ""
echo "📋 Comprobando ResponseEntity en Controllers..."
MISSING_RE=$(find src/main/java -path "*Controller.java" -exec grep -L "ResponseEntity" {} \; 2>/dev/null || true)
if [ -n "$MISSING_RE" ]; then
    echo "⚠️  Controllers sin ResponseEntity:"
    echo "$MISSING_RE"
    ISSUES_FOUND=$((ISSUES_FOUND + 1))
else
    echo "✅ Todos los Controllers usan ResponseEntity"
fi

# 5. Verificar que hay tests
echo ""
echo "📋 Comprobando cobertura de tests..."
MAIN_CLASSES=$(find src/main/java -name "*Service.java" -o -name "*Controller.java" | wc -l)
TEST_CLASSES=$(find src/test/java -name "*Test.java" | wc -l)
echo "   Clases principales: $MAIN_CLASSES"
echo "   Clases de test: $TEST_CLASSES"
if [ "$TEST_CLASSES" -lt "$MAIN_CLASSES" ]; then
    echo "⚠️  Faltan tests para algunas clases"
    ISSUES_FOUND=$((ISSUES_FOUND + 1))
else
    echo "✅ Cobertura de tests adecuada"
fi

# Resumen
echo ""
echo "============================================="
if [ "$ISSUES_FOUND" -gt 0 ]; then
    echo "⚠️  Se encontraron $ISSUES_FOUND problema(s) de convenciones"
    if [ "$CHECK_MODE" = "error" ]; then
        exit 1
    fi
else
    echo "✅ Todas las convenciones se cumplen correctamente"
fi
