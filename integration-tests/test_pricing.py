"""
Integration tests for the Pricing Engine.
These tests run the Java application as a subprocess and verify actual outputs.
"""

import subprocess
import sys
import os
import re

# المسار الجذري للمشروع
PROJECT_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))


def run_pricing_engine(prices, quantities, customer_type, discount_code):
    """
    تشغيل Java application وإرجاع المخرجات.
    """
    gradlew = "gradlew.bat" if sys.platform == "win32" else "./gradlew"
    gradlew_path = os.path.join(PROJECT_ROOT, gradlew)
    
    args = f"{prices} {quantities} {customer_type} {discount_code}"
    
    result = subprocess.run(
        [gradlew_path, "run", f"--args={args}", "-q"],
        cwd=PROJECT_ROOT,
        capture_output=True,
        text=True,
        shell=True
    )
    
    return result.stdout + result.stderr


def parse_breakdown(output):
    """
    استخراج القيم من output:
    Subtotal: 500.00, Discount: 0.00, Tax: 95.00, Final: 595.00
    """
    pattern = r"Subtotal:\s*([\d.]+).*?Discount:\s*([\d.]+).*?Tax:\s*([\d.]+).*?Final:\s*([\d.]+)"
    match = re.search(pattern, output)
    
    if not match:
        raise ValueError(f"Could not parse output:\n{output}")
    
    return {
        "subtotal": float(match.group(1)),
        "discount": float(match.group(2)),
        "tax": float(match.group(3)),
        "final": float(match.group(4)),
    }


def assert_close(actual, expected, tolerance=0.01, label=""):
    """مقارنة بدقة معينة."""
    assert abs(actual - expected) < tolerance, \
        f"{label}: expected {expected}, got {actual}"


# =============================
# 1. اختبارات بنية المشروع
# =============================

def test_project_structure():
    """التأكد من وجود الملفات الأساسية."""
    print("📁 Checking project structure...")
    
    required_files = [
        "build.gradle",
        "settings.gradle",
        "src/main/java/pricing/Main.java",
        "src/main/java/pricing/PricingEngine.java",
        "src/main/java/pricing/PricingEngine_bad.java",
        "src/main/java/pricing/CustomerType.java",
        "src/main/java/pricing/DiscountCode.java",
        "src/main/java/pricing/PriceBreakdown.java",
        "src/main/java/pricing/OrderCalculator.java",
        "src/main/java/pricing/DiscountService.java",
        "src/main/java/pricing/TaxService.java",
        "src/test/java/pricing/PricingEngineTest.java",
        "src/test/java/pricing/OrderCalculatorTest.java",
        "src/test/java/pricing/DiscountServiceTest.java",
        "src/test/java/pricing/TaxServiceTest.java",
    ]
    
    missing = []
    for file_path in required_files:
        full_path = os.path.join(PROJECT_ROOT, file_path)
        if os.path.exists(full_path):
            print(f"  ✓ {file_path}")
        else:
            print(f"  ✗ {file_path} (MISSING)")
            missing.append(file_path)
    
    assert not missing, f"Missing files: {missing}"
    print("✅ Project structure is correct\n")


# =============================
# 2. اختبارات JUnit
# =============================

def test_junit_tests_pass():
    """تشغيل JUnit tests والتأكد من نجاحها."""
    print("🧪 Running JUnit tests via Gradle...")
    
    gradlew = "gradlew.bat" if sys.platform == "win32" else "./gradlew"
    gradlew_path = os.path.join(PROJECT_ROOT, gradlew)
    
    result = subprocess.run(
        [gradlew_path, "test"],
        cwd=PROJECT_ROOT,
        capture_output=True,
        text=True,
        shell=True
    )
    
    output = result.stdout + result.stderr
    assert "BUILD SUCCESSFUL" in output, f"JUnit tests failed:\n{output}"
    assert "FAILED" not in output, f"Some tests failed:\n{output}"
    
    print("✅ All JUnit tests passed\n")


# =============================
# 3. اختبارات تكاملية حقيقية (تشغيل Java)
# =============================

def test_regular_customer_no_discount():
    """REGULAR customer, no discount: 500 + 19% tax = 595"""
    print("🧪 Test: Regular customer, no discount...")
    output = run_pricing_engine("[100.0,200.0]", "[1,2]", "REGULAR", "NONE")
    breakdown = parse_breakdown(output)
    
    assert_close(breakdown["subtotal"], 500.0, label="subtotal")
    assert_close(breakdown["discount"], 0.0, label="discount")
    assert_close(breakdown["tax"], 95.0, label="tax")
    assert_close(breakdown["final"], 595.0, label="final")
    
    print(f"  ✓ Subtotal: {breakdown['subtotal']}")
    print(f"  ✓ Discount: {breakdown['discount']}")
    print(f"  ✓ Tax: {breakdown['tax']}")
    print(f"  ✓ Final: {breakdown['final']}")
    print("✅ Test passed\n")


def test_save10_discount():
    """REGULAR + SAVE10: 100 - 10 = 90, +19% tax = 107.1"""
    print("🧪 Test: SAVE10 discount...")
    output = run_pricing_engine("[100.0]", "[1]", "REGULAR", "SAVE10")
    breakdown = parse_breakdown(output)
    
    assert_close(breakdown["subtotal"], 100.0, label="subtotal")
    assert_close(breakdown["discount"], 10.0, label="discount")
    assert_close(breakdown["final"], 107.1, label="final")
    
    print(f"  ✓ Discount: {breakdown['discount']}")
    print(f"  ✓ Final: {breakdown['final']}")
    print("✅ Test passed\n")


def test_vip_customer():
    """VIP customer (5% extra): 100 - 5 = 95, +19% tax = 113.05"""
    print("🧪 Test: VIP customer no code...")
    output = run_pricing_engine("[100.0]", "[1]", "VIP", "NONE")
    breakdown = parse_breakdown(output)
    
    assert_close(breakdown["discount"], 5.0, label="discount")
    assert_close(breakdown["final"], 113.05, label="final")
    
    print(f"  ✓ VIP Discount: {breakdown['discount']}")
    print(f"  ✓ Final: {breakdown['final']}")
    print("✅ Test passed\n")


def test_vip_with_save20():
    """VIP + SAVE20: 100 - 25 = 75, +19% tax = 89.25"""
    print("🧪 Test: VIP + SAVE20...")
    output = run_pricing_engine("[100.0]", "[1]", "VIP", "SAVE20")
    breakdown = parse_breakdown(output)
    
    assert_close(breakdown["discount"], 25.0, label="discount")
    assert_close(breakdown["final"], 89.25, label="final")
    
    print(f"  ✓ Combined Discount: {breakdown['discount']}")
    print(f"  ✓ Final: {breakdown['final']}")
    print("✅ Test passed\n")


def test_vip_with_save10_multiple_items():
    """نفس مثال Demo: VIP + SAVE10 + multiple items"""
    print("🧪 Test: VIP + SAVE10 + multiple items...")
    output = run_pricing_engine("[100.0,50.0]", "[2,1]", "VIP", "SAVE10")
    breakdown = parse_breakdown(output)
    
    assert_close(breakdown["subtotal"], 250.0, label="subtotal")
    assert_close(breakdown["discount"], 37.5, label="discount")
    assert_close(breakdown["tax"], 40.38, label="tax")
    assert_close(breakdown["final"], 252.88, label="final")
    
    print(f"  ✓ All values match expected\n")
    print("✅ Test passed\n")


def test_output_contains_all_fields():
    """التأكد من أن المخرجات تحتوي على كل الحقول المطلوبة."""
    print("🧪 Test: Output completeness...")
    output = run_pricing_engine("[200.0]", "[1]", "REGULAR", "SAVE10")
    
    assert "Subtotal" in output, "Output missing 'Subtotal'"
    assert "Discount" in output, "Output missing 'Discount'"
    assert "Tax" in output, "Output missing 'Tax'"
    assert "Final" in output, "Output missing 'Final'"
    
    print("  ✓ Contains: Subtotal, Discount, Tax, Final")
    print("✅ Test passed\n")


# =============================
# Runner
# =============================

if __name__ == "__main__":
    print("=" * 60)
    print("🚀 Running Real Integration Tests for Pricing Engine")
    print("=" * 60 + "\n")
    
    try:
        # 1. فحص الهيكل
        test_project_structure()
        
        # 2. تشغيل JUnit tests
        test_junit_tests_pass()
        
        print("=" * 60)
        print("📋 Running CLI integration tests (calling real Java)...")
        print("=" * 60 + "\n")
        
        # 3. اختبارات تكاملية حقيقية
        test_regular_customer_no_discount()
        test_save10_discount()
        test_vip_customer()
        test_vip_with_save20()
        test_vip_with_save10_multiple_items()
        test_output_contains_all_fields()
        
        print("=" * 60)
        print("🎉 All integration tests passed successfully!")
        print("=" * 60)
        sys.exit(0)
    except AssertionError as e:
        print(f"\n❌ Test failed: {e}")
        sys.exit(1)
    except Exception as e:
        print(f"\n💥 Unexpected error: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)